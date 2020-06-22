/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.core.internal.analysis.filter;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Filters classes and methods with annotation whose simple name contains
 * <code>ExcludeFromCodeCoverage</code>.
 */
public class ExcludeFromCodeCoverageFilter implements IFilter {

	@Override
	public void filter(MethodNode methodNode, IFilterContext context,
			IFilterOutput output) {
		for (String annotation : context.getClassAnnotations()) {
			if (matches(annotation)) {
				ignoreOutput(output, methodNode);
				return;
			}
		}
		if (presentIn(methodNode.visibleAnnotations)
				|| presentIn(methodNode.invisibleAnnotations)) {
			ignoreOutput(output, methodNode);
		}
	}

	private static void ignoreOutput(IFilterOutput output,
			MethodNode methodNode) {
		output.ignore(methodNode.instructions.getFirst(),
				methodNode.instructions.getLast());
	}

	private static boolean matches(final String annotation) {
		final String name = annotation
				.substring(Math.max(annotation.lastIndexOf('/'),
						annotation.lastIndexOf('$')) + 1);
		return name.contains("ExcludeFromCodeCoverage");
	}

	private static boolean presentIn(final List<AnnotationNode> annotations) {
		if (annotations != null) {
			for (AnnotationNode annotation : annotations) {
				if (matches(annotation.desc)) {
					return true;
				}
			}
		}
		return false;
	}

}
