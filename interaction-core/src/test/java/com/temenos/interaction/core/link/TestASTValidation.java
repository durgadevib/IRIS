package com.temenos.interaction.core.link;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TestASTValidation {

	@Test
	public void testValidateStatesValid() {
		ResourceState begin = new ResourceState("begin");
		ResourceState exists = new ResourceState("exists");
		ResourceState end = new ResourceState("end");
	
		begin.addTransition(new CommandSpec("create", "PUT"), exists);		
		exists.addTransition(new CommandSpec("delete", "DELETE"), end);
		
		Set<ResourceState> states = new HashSet<ResourceState>();
		states.add(begin);
		states.add(exists);
		states.add(end);
		
		ResourceStateMachine sm = new ResourceStateMachine(begin);
		ASTValidation v = new ASTValidation();
		assertTrue(v.validate(states, sm));	
	}

	@Test
	public void testValidateStatesUnreachable() {
		ResourceState begin = new ResourceState("begin");
		ResourceState exists = new ResourceState("exists");
		ResourceState end = new ResourceState("end");
	
		begin.addTransition(new CommandSpec("create", "PUT"), exists);		
		exists.addTransition(new CommandSpec("delete", "DELETE"), end);
		
		ResourceState unreachableState = new ResourceState("unreachable");
		Set<ResourceState> states = new HashSet<ResourceState>();
		states.add(begin);
		states.add(exists);
		states.add(unreachableState);
		states.add(end);
		
		ResourceStateMachine sm = new ResourceStateMachine(begin);
		ASTValidation v = new ASTValidation();
		assertFalse(v.validate(states, sm));	
	}

	@Test
	public void testDOT() {
		ResourceState begin = new ResourceState("begin");
		ResourceState exists = new ResourceState("exists");
		ResourceState end = new ResourceState("end");
	
		begin.addTransition(new CommandSpec("create", "PUT"), exists);		
		exists.addTransition(new CommandSpec("delete", "DELETE"), end);
				
		ResourceStateMachine sm = new ResourceStateMachine(begin);
		ASTValidation v = new ASTValidation();
		assertEquals("digraph G {\n    begin->exists[style=bold,label=create]\n    exists->end[style=bold,label=delete]\n}", v.graph(sm));	
	}

}