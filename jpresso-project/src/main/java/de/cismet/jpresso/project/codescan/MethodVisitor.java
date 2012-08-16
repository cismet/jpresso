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
package de.cismet.jpresso.project.codescan;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreePathScanner;

import org.netbeans.api.java.source.CompilationInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import de.cismet.jpresso.core.utils.TypeSafeCollections;

/**
 * Class to process a CompilationInfo and extract all public-static methods from it.
 *
 * <p>Used in AutoCompleteMethodProvider.</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class MethodVisitor extends TreePathScanner<List<String>, Void> {

    //~ Static fields/initializers ---------------------------------------------

    private static final Collection<Modifier> SEARCHED_MODIFIERS = Arrays.asList(
            new Modifier[] { Modifier.STATIC, Modifier.PUBLIC });
    private static final String DOUBLE_BRACKETS = "()";

    //~ Instance fields --------------------------------------------------------

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final CompilationInfo info;
    private final String classNamePrefix;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MethodVisitor object.
     *
     * @param  info  DOCUMENT ME!
     */
    public MethodVisitor(final CompilationInfo info) {
        this.info = info;
        this.classNamePrefix = info.getFileObject().getName() + ".";
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Iterates through the CompilationInfo and returns a list of all found public-static-methods.
     *
     * @param   t  DOCUMENT ME!
     * @param   v  DOCUMENT ME!
     *
     * @return  list of all public-static-methods
     */
    @Override
    public List<String> visitClass(final ClassTree t, final Void v) {
        final List<String> methods = TypeSafeCollections.newLinkedList();
        final Element el = info.getTrees().getElement(getCurrentPath());
        if (el == null) {
            log.warn("Error while scanning for public-static methods: cannot resolve class " + classNamePrefix);
        } else {
            final TypeElement te = (TypeElement)el;
            final List<ExecutableElement> temp = ElementFilter.methodsIn(te.getEnclosedElements());
            for (final ExecutableElement e : temp) {
                if (e.getModifiers().containsAll(SEARCHED_MODIFIERS)) {
                    methods.add(classNamePrefix + e.getSimpleName() + DOUBLE_BRACKETS);
                }
            }
        }
        return methods;
    }
}
