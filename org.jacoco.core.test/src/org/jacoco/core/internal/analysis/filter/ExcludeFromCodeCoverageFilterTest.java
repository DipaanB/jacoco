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

import org.jacoco.core.internal.instr.InstrSupport;
import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link ExcludeFromCodeCoverageFilter}.
 */
public class ExcludeFromCodeCoverageFilterTest extends FilterTestBase {
	private final IFilter filter = new ExcludeFromCodeCoverageFilter();

	private AbstractInsnNode fromInclusive;
	private AbstractInsnNode toInclusive;

	@Test
	public void should_filter_exclude_annotation() {
		final MethodNode m = new MethodNode(InstrSupport.ASM_API_VERSION, 0,
				"hashCode", "()I", null, null);
		m.visitAnnotation("LExcludeFromCodeCoverage;", false);

		m.visitInsn(Opcodes.ICONST_0);
		m.visitInsn(Opcodes.IRETURN);

		filter.filter(m, context, output);

		assertMethodIgnored(m);
	}

	@Test
	public void should_filter_exclude_annotation_in_inner_class() {
		final MethodNode m = new MethodNode(InstrSupport.ASM_API_VERSION, 0,
				"hashCode", "()I", null, null);
		m.visitAnnotation(
				"Lsomething/anything/SomeClass$ExcludeFromCodeCoverage;",
				false);

		m.visitInsn(Opcodes.ICONST_0);
		m.visitInsn(Opcodes.IRETURN);

		filter.filter(m, context, output);

		assertMethodIgnored(m);
	}

	@Test
	public void should_filter_exclude_annotation_with_package() {
		final MethodNode m = new MethodNode(InstrSupport.ASM_API_VERSION, 0,
				"hashCode", "()I", null, null);
		m.visitAnnotation("Lsomething/anything/ExcludeFromCodeCoverage;",
				false);

		m.visitInsn(Opcodes.ICONST_0);
		m.visitInsn(Opcodes.IRETURN);

		filter.filter(m, context, output);

		assertMethodIgnored(m);
	}

	@Test
	public void should_not_filter_no_annotations() {
		final MethodNode m = new MethodNode(InstrSupport.ASM_API_VERSION, 0,
				"hashCode", "()I", null, null);

		m.visitInsn(Opcodes.ICONST_0);
		m.visitInsn(Opcodes.IRETURN);

		filter.filter(m, context, output);

		assertIgnored();
	}

	@Test
	public void should_not_filter_other_annotation() {
		final MethodNode m = new MethodNode(InstrSupport.ASM_API_VERSION, 0,
				"hashCode", "()I", null, null);
		m.visitAnnotation("Lother/Annotation;", false);

		m.visitInsn(Opcodes.ICONST_0);
		m.visitInsn(Opcodes.IRETURN);

		filter.filter(m, context, output);

		assertIgnored();
	}

}
