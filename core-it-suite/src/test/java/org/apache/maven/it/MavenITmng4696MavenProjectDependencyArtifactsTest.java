package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.junit.jupiter.api.Test;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.HashSet;
import java.util.Properties;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-4696">MNG-4696</a>.
 *
 * @author Benjamin Bentmann
 */
public class MavenITmng4696MavenProjectDependencyArtifactsTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4696MavenProjectDependencyArtifactsTest()
    {
        super( "[2.0.3,3.0-alpha-1),[3.0-beta-2,)" );
    }

    /**
     * Verify that MavenProject.getDependencyArtifacts() returns all direct dependencies regardless of their scope.
     * In other words, getDependencyArtifacts() is in general not a subset of MavenProject.getArtifacts() as the
     * latter is subject to scope filtering as requested by plugins.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4696" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteArtifacts( "org.apache.maven.its.mng4696" );
        verifier.addCliOption( "-s" );
        verifier.addCliOption( "settings.xml" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.executeGoal( "initialize" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/artifact.properties" );
        assertEquals( "3", props.getProperty( "project.dependencyArtifacts.size" ) );

        HashSet<String> ids = new HashSet<>();
        ids.add( props.getProperty( "project.dependencyArtifacts.0.artifactId" ) );
        ids.add( props.getProperty( "project.dependencyArtifacts.1.artifactId" ) );
        ids.add( props.getProperty( "project.dependencyArtifacts.2.artifactId" ) );
        assertTrue( ids.toString(), ids.contains( "b" ) );
        assertTrue( ids.toString(), ids.contains( "c" ) );
        assertTrue( ids.toString(), ids.contains( "d" ) );
    }

}
