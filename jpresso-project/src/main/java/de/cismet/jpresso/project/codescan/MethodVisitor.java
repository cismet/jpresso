/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.jpresso.project.codescan;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreePathScanner;
import de.cismet.jpresso.core.utils.TypeSafeCollections;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.netbeans.api.java.source.CompilationInfo;

/**
 * Class to process a CompilationInfo and extract all public-static methods from it.
 * 
 * Used in AutoCompleteMethodProvider.
 * 
 * @author srichter
 */
public final class MethodVisitor extends TreePathScanner<List<String>, Void> {

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final CompilationInfo info;
    private final String classNamePrefix;
    private static final Collection<Modifier> SEARCHED_MODIFIERS = Arrays.asList(new Modifier[]{Modifier.STATIC, Modifier.PUBLIC});
    private static final String DOUBLE_BRACKETS = "()";

    /**
     * 
     * @param info
     */
    public MethodVisitor(final CompilationInfo info) {
        this.info = info;
        this.classNamePrefix = info.getFileObject().getName() + ".";
    }

    /**
     * Iterates through the CompilationInfo and returns a list of all found 
     * public-static-methods.
     * 
     * @param t
     * @param v
     * @return list of all public-static-methods
     */
    @Override
    public final List<String> visitClass(final ClassTree t, Void v) {
        final List<String> methods = TypeSafeCollections.newLinkedList();
        final Element el = info.getTrees().getElement(getCurrentPath());
        if (el == null) {
            log.warn("Error while scanning for public-static methods: cannot resolve class " + classNamePrefix);
        } else {
            final TypeElement te = (TypeElement) el;
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
