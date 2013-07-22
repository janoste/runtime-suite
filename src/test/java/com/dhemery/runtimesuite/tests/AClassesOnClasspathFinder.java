package com.dhemery.runtimesuite.tests;

import java.io.File;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhemery.runtimesuite.finders.ClassesOnClasspath;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AClassesOnClasspathFinder {
	Logger log = LoggerFactory.getLogger(ClassesOnClasspath.class);

	@Before public void printSystemClasspath() {
		log.debug("System java.class.path is {}", System.getProperty("java.class.path"));
	}

	@SuppressWarnings("unchecked")
	@Test public void findsAllClassesOnASingleElementClasspath() {
		String classpath = "build/classpath.a";
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(foundClasses,  hasItems(	classpath.a.Test_a_1.class,
											classpath.a.Test_a_2.class,
											classpath.a.a.Test_aa_1.class,
											classpath.a.a.Test_aa_2.class,
											classpath.a.a.a.Test_aaa_1.class,
											classpath.a.a.a.Test_aaa_2.class,
											classpath.a.a.b.Test_aab_1.class,
											classpath.a.a.b.Test_aab_2.class,
											classpath.a.b.Test_ab_1.class,
											classpath.a.b.Test_ab_2.class));
	}

	@SuppressWarnings("unchecked")
	@Test public void findsAllClassesOnAMultipleElementClasspath() {
		String classpath = "build/classpath.a"
						+ File.pathSeparator
						+ "build/classpath.b";
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(foundClasses, hasItems(	classpath.a.Test_a_1.class,
		        classpath.a.Test_a_2.class,
		        classpath.a.a.Test_aa_1.class,
		        classpath.a.a.Test_aa_2.class,
		        classpath.a.a.a.Test_aaa_1.class,
		        classpath.a.a.a.Test_aaa_2.class,
		        classpath.a.a.b.Test_aab_1.class,
		        classpath.a.a.b.Test_aab_2.class,
		        classpath.a.b.Test_ab_1.class,
		        classpath.a.b.Test_ab_2.class,
		        classpath.b.Test_b_1.class,
		        classpath.b.Test_b_2.class,
		        classpath.b.a.Test_ba_1.class,
		        classpath.b.a.Test_ba_2.class,
		        classpath.b.b.Test_bb_1.class,
		        classpath.b.b.Test_bb_2.class,
		        classpath.b.b.a.Test_bba_1.class,
		        classpath.b.b.a.Test_bba_2.class,
		        classpath.b.b.b.Test_bbb_1.class,
		        classpath.b.b.b.Test_bbb_2.class));
	}
	
	@Test public void ignoresNonClassFiles() {
		// classpath.c contains
		//    - ./c/Test_c_1.class
		//    - ./c/not-a-test.txt
		String classpath = "build/classpath.c";
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(foundClasses, hasItem(classpath.c.Test_c_1.class));
	}
	
	@Test public void ignoresNonTestClasses() {
		// classpath.d contains
		//    - ./d/Test_d_1.class
		//    - ./d/NotATest_d_2.class
		String classpath = "build/classpath.d";
		Collection<Class<?>> foundClasses = new ClassesOnClasspath(classpath).find();
		assertThat(foundClasses, hasItem(classpath.d.Test_d_1.class));
	}
	
	@Test public void ignoresNonDirectoryClasspathElements() {
		Collection<Class<?>> foundClasses = new ClassesOnClasspath("no.such.directory").find();
		assertThat(foundClasses, hasSize(0));
	}
}
