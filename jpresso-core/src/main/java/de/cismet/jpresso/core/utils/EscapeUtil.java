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
package de.cismet.jpresso.core.utils;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class EscapeUtil {

    //~ Static fields/initializers ---------------------------------------------

    private static final char[] VALID_JAVA_SPECIAL_VAR_CHAR = new char[] { 'ä', 'ö', 'ü', 'Ä', 'Ö', 'Ü', 'ß' };

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EscapeUtil object.
     *
     * @throws  AssertionError  DOCUMENT ME!
     */
    private EscapeUtil() {
        throw new AssertionError();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   str  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String escapeJavaVariableName(final String str) {
        if (str == null) {
            return null;
        }
        final StringBuffer sb = new StringBuffer(str.length());
        for (final char c : str.toCharArray()) {
            if (isValidJavaVariableCharacter(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static boolean isValidJavaVariableCharacter(final char c) {
        if ((((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9')) || (c == '$')
                        || (c == '_'))) {
            return true;
        } else {
            for (final char cur : VALID_JAVA_SPECIAL_VAR_CHAR) {
                if (cur == c) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   str  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String escapeJava(final String str) {
        if (str == null) {
            return null;
        }
        final int sz;
        sz = str.length();
        final StringBuffer out = new StringBuffer(sz * 2);
        for (int i = 0; i < sz; i++) {
            final char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.append("\\u" + hex(ch));
            } else if (ch > 0xff) {
                out.append("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                out.append("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b': {
                        out.append('\\');
                        out.append('b');
                        break;
                    }
                    case '\n': {
                        out.append('\\');
                        out.append('n');
                        break;
                    }
                    case '\t': {
                        out.append('\\');
                        out.append('t');
                        break;
                    }
                    case '\f': {
                        out.append('\\');
                        out.append('f');
                        break;
                    }
                    case '\r': {
                        out.append('\\');
                        out.append('r');
                        break;
                    }
                    default: {
                        if (ch > 0xf) {
                            out.append("\\u00" + hex(ch));
                        } else {
                            out.append("\\u000" + hex(ch));
                        }
                        break;
                    }
                }
            } else {
                switch (ch) {
                    case '\'': {
                        out.append('\'');
                        break;
                    }
                    case '"': {
                        out.append('\\');
                        out.append('"');
                        break;
                    }
                    case '\\': {
                        out.append('\\');
                        out.append('\\');
                        break;
                    }
                    default: {
                        out.append(ch);
                        break;
                    }
                }
            }
        }
        return out.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ch  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String hex(final char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }
}
