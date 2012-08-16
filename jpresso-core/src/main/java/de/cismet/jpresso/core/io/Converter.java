/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.core.io;

import code.AssignerBase;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.cismet.jpresso.core.data.DatabaseConnection;
import de.cismet.jpresso.core.data.JPressoRun;
import de.cismet.jpresso.core.data.Mapping;
import de.cismet.jpresso.core.data.Options;
import de.cismet.jpresso.core.data.Query;
import de.cismet.jpresso.core.data.Reference;
import de.cismet.jpresso.core.data.RuntimeProperties;
import de.cismet.jpresso.core.deprecated.castorGenerated.Prop;
import de.cismet.jpresso.core.deprecated.castorGenerated.RuntimeProps;
import de.cismet.jpresso.core.exceptions.InvalidFormatFileException;
import de.cismet.jpresso.core.finalizer.StandardFinalizer;
import de.cismet.jpresso.core.kernel.ImportMetaInfo;
import de.cismet.jpresso.core.serviceprovider.JPressoFileManager;
import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Imports old "ImportAnt" files into a JPresso project.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class Converter {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Converter.class);
    public static final Pattern FUNCTION_PATTERN = Pattern.compile("(\\w+\\s*\\()[\\w\\s,]*\\)\\s*\\{");
    public static final String FUNCTION_CROSS_REFERENCE = "cidsCrossReference";
    public static final String FUNCTION_BREAK_IT = "cidsBreakIf";
//    public static final Pattern FUNCTION_INTERNAL_1 = Pattern.compile("^[\\s]*" + FUNCTION_CROSS_REFERENCE + "\\(\"([\\p{Alnum}_]+)\\$FROM\\$([^\\$]+)\\$\\.([\\p{Alnum}_]+)\"\\)");
    public static final Pattern FUNCTION_CROSS_REFERENCE_PATTERN = Pattern.compile("^[\\s]*" + FUNCTION_CROSS_REFERENCE
                    + "\\(\"(.+)\"\\)");
    public static final Pattern FUNCTION_INTERNAL_1 = Pattern.compile(
            "([\\p{Alnum}_]+)\\$FROM\\$([^\\$]+)\\$\\.([\\p{Alnum}_]+)");
    public static final Pattern FUNCTION_INTERNAL_2 = Pattern.compile("([\\p{Alnum}_]+)\\.([\\p{Alnum}_]+)");
    public static final Pattern BREAK_PATTERN = Pattern.compile(".*" + FUNCTION_BREAK_IT + ".*");

    //~ Methods ----------------------------------------------------------------

    /**
     * Imports old "ImportAnt" files into a JPresso project.
     *
     * @param   inputFile   DOCUMENT ME!
     * @param   projDir     DOCUMENT ME!
     * @param   mergeProps  DOCUMENT ME!
     *
     * @throws  IOException                 DOCUMENT ME!
     * @throws  InvalidFormatFileException  DOCUMENT ME!
     */
    public static void convertOldImportFile(final String inputFile, final String projDir, final String mergeProps)
            throws IOException, InvalidFormatFileException {
        String msg;
        final String[] merge = mergeProps.split(";");
        if ((inputFile == null) || (projDir == null)) {
            msg = "Start converting ...\nError: Insufficient properties: " + inputFile + ", " + projDir + ", "
                        + mergeProps;
            log.info(msg);
            return;
        }
        boolean mergeConnections = false;
        boolean mergeQueries = false;
        boolean mergeCode = false;

        final Map<DatabaseConnection, File> connectionHash = TypeSafeCollections.newHashMap();
        final Map<Query, File> queryHash = TypeSafeCollections.newHashMap();
        String className = null;
        File codeFile = null;
        final Set<String> imports = TypeSafeCollections.newHashSet();
        final StringBuilder codeBuilder = new StringBuilder();
        final JPressoFileManager manager = XStreamJPressoFileManager.getInstance();
        if ((merge != null) && (merge.length == 3)) {
            try {
                mergeConnections = Boolean.parseBoolean(merge[0].trim());
                mergeQueries = Boolean.parseBoolean(merge[1].trim());
                mergeCode = Boolean.parseBoolean(merge[2].trim());
            } catch (Exception e) {
                log.error(e, e);
            }
        }
        final String[] files = inputFile.split(";");
        msg = "Start converting ...\nMerging: ";
        if (mergeConnections) {
            msg += "Connections ";
        }
        if (mergeQueries) {
            msg += "Queries ";
        }
        if (mergeCode) {
            msg += "Code ";
        }
        if (!mergeCode && !mergeConnections && !mergeQueries) {
            msg += "Nothing";
        }
        log.info(msg);
        System.out.println(msg);
        final File rootDir = new File(projDir);
        rootDir.mkdir();
        final File queryDir = new File(rootDir.getAbsolutePath() + File.separator + JPressoFileManager.DIR_QRY);
        final File codeDir = new File(rootDir.getAbsolutePath() + File.separator + JPressoFileManager.DIR_CDE);
        final File connectionDir = new File(rootDir.getAbsolutePath() + File.separator + JPressoFileManager.DIR_CON);
        final File runDir = new File(rootDir.getAbsolutePath() + File.separator + JPressoFileManager.DIR_RUN);
        final File projectDir = new File(rootDir.getAbsolutePath() + File.separator + JPressoFileManager.DIR_JPP);

        if (mergeCode) {
            codeFile = JPressoFileManager.getDefault()
                        .findFreeFile(new File(codeDir, "Std." + JPressoFileManager.END_JAVA));
            className = codeFile.getName().substring(0, codeFile.getName().length() - 5);
        }
        for (final String cur : files) {
            final File oldFile = new File(cur);
            FileReader r = null;
            // ----Verzeichnisse anlegen---
// queryDir.mkdir();
// codeDir.mkdir();
// connectionDir.mkdir();
// runDir.mkdir();
// projectDir.mkdir();
            // ----Verzeichnisse anlegen---
            try {
                // aus dem XML File die entsprechende Datenstruktur machen (CASTOR)
                msg = "- Opening old formated file ..." + oldFile;
                log.info(msg);
                System.out.println(msg);
                r = new FileReader(oldFile);
                msg = "- Parsing old import rules ...";
                log.info(msg);
                System.out.println(msg);
                final de.cismet.jpresso.core.deprecated.castorGenerated.ImportRules impRules =
                    de.cismet.jpresso.core.deprecated.castorGenerated.ImportRules.unmarshal(r);
                r.close();

// <editor-fold defaultstate="collapsed" desc="Preparations">
                // ---------------------------prepare
                msg = "- Converting runtime properties ...";
                log.info(msg);
                System.out.println(msg);
                final JPressoRun myRules = new JPressoRun();
                final de.cismet.jpresso.core.deprecated.castorGenerated.Code code = impRules.getCode();
                final de.cismet.jpresso.core.deprecated.castorGenerated.ConnectionInfo info =
                    impRules.getConnectionInfo();
                final de.cismet.jpresso.core.deprecated.castorGenerated.Options options = impRules.getOptions();
                final de.cismet.jpresso.core.deprecated.castorGenerated.PreProcessingAndMapping mappings =
                    impRules.getPreProcessingAndMapping();
                final de.cismet.jpresso.core.deprecated.castorGenerated.Relations rel = impRules.getRelations();
                final de.cismet.jpresso.core.deprecated.castorGenerated.RuntimeProps props = impRules.getRuntimeProps();

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Options">
                // ---------------------------options
                // String [] optionsNormalize = options.getNormalize();
                msg = "- Converting normalisation ...";
                log.info(msg);
                System.out.println(msg);
                final Options myOps = new Options();
                if (options != null) {
                    for (final String s : options.getNormalize()) {
                        myOps.getNormalize().add(s);
                    }
                }
                myRules.setOptions(myOps);

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Relations">
                // ---------------------------relations
                msg = "- Converting relations ...";
                log.info(msg);
                System.out.println(msg);
                if (rel != null) {
                    final de.cismet.jpresso.core.deprecated.castorGenerated.Relation[] rels = rel.getRelation();
                    // ---------------------------
                    final List<Reference> myRels = TypeSafeCollections.newArrayList();
                    for (final de.cismet.jpresso.core.deprecated.castorGenerated.Relation re : rels) {
                        final Reference myRel = new Reference();
                        myRel.setReferencingTable(re.getMasterTable());
                        myRel.setReferencedTable(re.getDetailTable());
                        myRel.setReferencedField(re.getDetailTableKey());
                        myRel.setEnclosingChar(re.getEnclosingChar());
                        myRel.setComparing(re.getForeignKeyComparing());
                        myRel.setReferencingField(re.getMasterTableForeignKey());
                        myRels.add(myRel);
                    }
                    myRules.setReferences(myRels);
                }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Code">
                // ---------------------------code
                msg = "- Converting functions ...";
                log.info(msg);
                System.out.println(msg);
                List<String> replaceList = TypeSafeCollections.newArrayList();
                if (code != null) {
                    final de.cismet.jpresso.core.deprecated.castorGenerated.Function[] fus = code.getFunction();
                    final StringBuilder functions = new StringBuilder();
                    // ---------------------------function
                    for (final de.cismet.jpresso.core.deprecated.castorGenerated.Function f : fus) {
                        functions.append("public static ");
                        functions.append(f.getContent().replaceAll(
                                "universalContainer",
                                ImportMetaInfo.FIELD_TAG.replaceAll("\\$", "\\\\\\$")
                                        + "UniversalContainer").replaceAll(
                                "targetConnection",
                                ImportMetaInfo.FIELD_TAG.replaceAll("\\$", "\\\\\\$")
                                        + "TargetConnection").trim());
                        functions.append("\n\n");
//                    Function myFunct = new Function();
//                    myFunct.setComment(f.getComment());
//                    myFunct.setContent(f.getContent().replaceAll(
//                            "universalContainer", ImportMetaInfo.FIELD_TAG + "universalContainer").replaceAll(
//                            "targetConnection", ImportMetaInfo.FIELD_TAG + "targetConnection"));
//                    myCode.getFunctions().add(myFunct);
                    }
                    replaceList = getFunctionList(code);
                    if (mergeCode) {
                        if (code.getImport() != null) {
                            final String[] imps = code.getImport()
                                        .replaceAll("import[\\s]+de\\.cismet\\.cids\\.admin\\.importAnt[^\\n]*;", "")
                                        .split(";");
                            if (imps != null) {
                                for (final String i : imps) {
                                    imports.add(i);
                                }
                            }
                            if (functions != null) {
                                codeBuilder.append(functions);
                            }
                        }
                    } else {
                        try {
                            final String importString = (code.getImport() != null)
                                ? code.getImport()
                                        .replaceAll("import[\\s]+de\\.cismet\\.cids\\.admin\\.importAnt[^\\n]*;", "")
                                : "";
                            codeFile = JPressoFileManager.getDefault()
                                        .findFreeFile(new File(codeDir, "Std." + JPressoFileManager.END_JAVA));
                            className = codeFile.getName().substring(0, codeFile.getName().length() - 5);
                            final BufferedWriter out = new BufferedWriter(new FileWriter(codeFile));
                            out.write("package code;\n\n" + importString + "\n\npublic class " + className + " extends "
                                        + AssignerBase.class.getCanonicalName() + " {\n\n" + functions.toString()
                                        + "}");
                            out.close();
                        } catch (IOException e) {
                            log.error(e, e);
                        }
                    }
                }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Mappings">
                // ---------------------------mapping
                log.info("- Converting mappings ...");
                if (mappings != null) {
                    final de.cismet.jpresso.core.deprecated.castorGenerated.Mapping[] mapping = mappings.getMapping();
                    // ---------------------------
                    final ArrayList<Mapping> maps = TypeSafeCollections.newArrayList();
                    for (final de.cismet.jpresso.core.deprecated.castorGenerated.Mapping map : mapping) {
                        final Matcher crossRefMatcher = FUNCTION_CROSS_REFERENCE_PATTERN.matcher(map.getContent()
                                        .trim());
                        if (crossRefMatcher.matches()) {
                            final String match = crossRefMatcher.group(1);
                            final Matcher m1 = FUNCTION_INTERNAL_1.matcher(match);
                            if (m1.matches()) {
                                final String masterTable = map.getTargetTable();
                                final String masterField = map.getTargetField();
                                final String detailTable = m1.group(1);
                                final String path = "[" + m1.group(2) + "]";
                                final String detailField = m1.group(3);
                                final Reference createdRelation = new Reference();
                                createdRelation.setReferencedTable(detailTable + path);
                                createdRelation.setReferencedField(detailField);
                                createdRelation.setReferencingTable(masterTable);
                                createdRelation.setReferencingField(masterField);
                                createdRelation.setComparing(map.getComparing());
                                createdRelation.setEnclosingChar(map.getEnclosingChar());
                                myRules.getReferences().add(createdRelation);
                            } else {
                                final Matcher m2 = FUNCTION_INTERNAL_2.matcher(match);
                                if (m2.matches()) {
                                    final String masterTable = map.getTargetTable();
                                    final String masterField = map.getTargetField();
                                    final String detailTable = m2.group(1);
                                    final String detailField = m2.group(2);
                                    final Reference createdRelation = new Reference();
                                    createdRelation.setReferencedTable(detailTable);
                                    createdRelation.setReferencedField(detailField);
                                    createdRelation.setReferencingTable(masterTable);
                                    createdRelation.setReferencingField(masterField);
                                    createdRelation.setComparing(map.getComparing());
                                    createdRelation.setEnclosingChar(map.getEnclosingChar());
                                    myRules.getReferences().add(createdRelation);
                                } else {
                                    msg = "\t!!! WARNING: Possible crossReference problem on: " + match;
                                    log.warn(msg);
                                    System.out.println(msg);
                                }
                            }
                        } else {
                            final Matcher breakMatch = BREAK_PATTERN.matcher(map.getContent());
                            if (breakMatch.matches()) {
                                msg = "\t!!! WARNING: Found usage of cids-break: " + map.getContent();
                                log.warn(msg);
                                System.out.println(msg);
                            }
                            final Mapping myMap = new Mapping();
                            myMap.setAutoIncrement(map.getAutoIncrement());
                            myMap.setComparing(map.getComparing());
                            String content = map.getContent();
                            if (className != null) {
                                for (final String s : replaceList) {
                                    content = content.replace(s, className + "." + s);
                                }
                            }
                            myMap.setContent(content);
                            myMap.setEnclosingChar(map.getEnclosingChar());
                            if (map.getPath() != null) {
                                myMap.setPath(map.getPath().replaceAll("/", ""));
                            }
                            myMap.setTargetField(map.getTargetField());
                            myMap.setTargetTable(map.getTargetTable());
                            maps.add(myMap);
                        }
                    }
                    myRules.setMappings(maps);
                }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Connections">
                // ---------------------------conInfo
                final de.cismet.jpresso.core.deprecated.castorGenerated.SourceJdbcConnectionInfo source =
                    info.getSourceJdbcConnectionInfo();
                final de.cismet.jpresso.core.deprecated.castorGenerated.TargetJdbcConnectionInfo target =
                    info.getTargetJdbcConnectionInfo();
                File queryFile = null;
                File queryConFile = null;
                File targetConFile = null;

                if (target != null) {
                    final DatabaseConnection myTgt = new DatabaseConnection();
                    myTgt.setDriverClass(target.getDriverClass());
//                myTgt.setPass(target.getPass());
                    myTgt.setUrl(target.getUrl());
//                myTgt.setUser(target.getUser());
                    if (target.getProp() != null) {
                        for (final Prop prp : target.getProp()) {
                            myTgt.getProps().setProperty(prp.getKey(), prp.getContent());
                        }
                    }

                    if (mergeConnections) {
                        for (final Entry<DatabaseConnection, File> entry : connectionHash.entrySet()) {
                            if (entry.getKey().weakEquals(myTgt)) {
                                targetConFile = entry.getValue();
                                break;
                            }
                        }
                    }

                    if (targetConFile == null) {
                        targetConFile = new File(connectionDir.getAbsolutePath() + File.separator + "tCon_"
                                        + oldFile.getName().replace(".", "_") + "."
                                        + JPressoFileManager.END_CONNECTION);
                        msg = "\t* Writing Target-Connection file to " + targetConFile;
                        log.info(msg);
                        System.out.println(msg);
                        manager.persist(manager.findFreeFile(targetConFile), myTgt);
                        connectionHash.put(myTgt, targetConFile);
                    } else {
                        msg = "\t* Target-Connection file normalized with connection " + targetConFile;
                        log.info(msg);
                        System.out.println(msg);
                    }
                    myRules.setTargetConnection(targetConFile.getName());
                }

                if (source != null) {
                    final Query mySrc = new Query();
                    mySrc.setDriverClass(source.getDriverClass());
                    mySrc.setUrl(source.getUrl());
                    mySrc.setQueryStatement(source.getStatement());
                    if (source.getProp() != null) {
                        for (final Prop prp : source.getProp()) {
                            mySrc.getProps().setProperty(prp.getKey(), prp.getContent());
                        }
                    }

                    final DatabaseConnection queryCon = new DatabaseConnection(mySrc.getDriverClass(),
                            mySrc.getUrl(),
                            mySrc.getProps());

                    if (mergeConnections) {
                        for (final Entry<DatabaseConnection, File> entry : connectionHash.entrySet()) {
                            if (entry.getKey().deepEquals(queryCon)) {
                                queryConFile = entry.getValue();
                                break;
                            }
                        }
                    }
                    if (mergeQueries) {
                        for (final Entry<Query, File> entry : queryHash.entrySet()) {
                            if (entry.getKey().weakEquals(mySrc)) {
                                queryFile = entry.getValue();
                                break;
                            }
                        }
                    }
                    msg = "- Writing file outputs: ";
                    log.info(msg);
                    System.out.println(msg);
                    if (queryConFile == null) {
                        queryConFile = new File(connectionDir.getAbsolutePath() + File.separator + "qCon_"
                                        + oldFile.getName().replace(".", "_") + "."
                                        + JPressoFileManager.END_CONNECTION);
                        mySrc.setConnectionFile(queryConFile.getName());

                        msg = "\t* Writing Source-Connection to " + queryConFile;
                        log.info(msg);
                        System.out.println(msg);
                        manager.persist(manager.findFreeFile(queryConFile), queryCon);
                        connectionHash.put(queryCon, queryConFile);
                    } else {
                        msg = "\t* Source-Connection file normalized with connection " + queryConFile;
                        log.info(msg);
                        System.out.println(msg);
                        mySrc.setConnectionFile(queryConFile.getName());
                    }
                    if (queryFile == null) {
                        queryFile = new File(queryDir.getAbsolutePath() + File.separator + "query_"
                                        + oldFile.getName().replace(".", "_") + "." + JPressoFileManager.END_QUERY);
                        msg = "\t* Writing Source-Query to " + queryFile;
                        log.info(msg);
                        System.out.println(msg);
                        manager.persist(manager.findFreeFile(queryFile), mySrc);
                        queryHash.put(mySrc, queryFile);
                    } else {
                        msg = "\t* Source-Query file normalized with query " + queryFile;
                        log.info(msg);
                        System.out.println(msg);
                    }
                    myRules.setSourceQuery(queryFile.getName());
                }

                // ---------------------------
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="RuntimeProperties">
                // System.out.println("Zuweisungen, Formatumwandlungen, Normalisierung ...");
                log.info("Parsing runtime properties");
                RuntimeProperties rProp = new RuntimeProperties();
                // rProp.setImportFileName(oldFile.getAbsolutePath());
                final RuntimeProps rp = impRules.getRuntimeProps();
                rProp = parseXMLRuntimeProps(rp);
//            RuntimeProperties rps = new RuntimeProperties();
//            rps.setFinalizerClass(rProp.getFinalizerClass());
//            rps.setImportFileName(rProp.getImportFileName());
//            rps.setKeepAssigner(rProp.isKeepAssigner());
//            rps.setTmpDirectory(rProp.getTmpDirectory());
//            rps.setFinalizerProperties(rProp.getFinalizerProperties());
                myRules.setRuntimeProperties(rProp);
// </editor-fold>
                final File runFile = new File(runDir.getAbsolutePath() + File.separator + "run_"
                                + oldFile.getName().replace(".", "_") + "." + JPressoFileManager.END_RUN);
                msg = "\t* Writing Run to " + runFile;
                log.info(msg);
                System.out.println(msg);
                manager.persist(manager.findFreeFile(runFile), myRules);
                msg = "- Converting successfully completed.\nDone.\n";
                log.info(msg);
                System.out.println(msg);
            } catch (MarshalException ex) {
                log.error("Invalid file format: Could not parse or convert.", ex);
                throw new InvalidFormatFileException("Invalid file format: Could not parse or convert.");
            } catch (ValidationException ex) {
                log.error("Invalid file format: Could not parse or convert.", ex);
                throw new InvalidFormatFileException("Invalid file format: Could not parse or convert.");
            } catch (Exception ex) {
                log.error("Invalid file format: Could not parse or convert.", ex);
                throw new InvalidFormatFileException("Invalid file format: Could not parse or convert.");
            } finally {
                try {
                    r.close();
                } catch (IOException ex) {
                    log.error("Can not close file.", ex);
                }
            }
        }
        if (mergeCode) {
            try {
                final BufferedWriter out = new BufferedWriter(new FileWriter(codeFile));
                out.write("package code;\n\n");
                for (final String s : imports) {
                    out.write(s + ";\n");
                }
                out.write("\n\npublic class " + className + " extends " + AssignerBase.class.getCanonicalName()
                            + " {\n\n" + codeBuilder + "}");
                out.close();
            } catch (IOException e) {
                log.error(e, e);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   rp  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private static RuntimeProperties parseXMLRuntimeProps(final RuntimeProps rp) throws Exception {
        final RuntimeProperties rProp = new RuntimeProperties();
        if (rp != null) {
            final de.cismet.jpresso.core.deprecated.castorGenerated.Finalizer finalizer = rp.getFinalizer();
            rProp.setFinalizerClass(StandardFinalizer.class.getSimpleName());

            final Properties finP = new Properties();
            // cast
            final Enumeration<Prop> en = finalizer.enumerateProp();
            while (en.hasMoreElements()) {
                final Prop p = en.nextElement();
                final String key = p.getKey();
                final String content = p.getContent();
                finP.setProperty(key, content);
            }
            rProp.setFinalizerProperties(finP);
        } else {
            throw new Exception("keine Runtime Properties in XML-Datei");
        }
        return rProp;
    }

    /**
     * A more or less heuristic function that finds function-heads from code.
     *
     * <p>TODO: can not yet handle comments WITHIN the function head!</p>
     *
     * @param   code  DOCUMENT ME!
     *
     * @return  list of heuristically found functions.
     */
    public static List<String> getFunctionList(final de.cismet.jpresso.core.deprecated.castorGenerated.Code code) {
        final List<String> ret = TypeSafeCollections.newArrayList();
        for (final de.cismet.jpresso.core.deprecated.castorGenerated.Function f : code.getFunction()) {
            final Matcher m = FUNCTION_PATTERN.matcher(f.getContent());
            if (m.find()) {
                final String toAdd = m.group(m.groupCount());
                ret.add(toAdd);
            }
        }
        return ret;
    }
}
