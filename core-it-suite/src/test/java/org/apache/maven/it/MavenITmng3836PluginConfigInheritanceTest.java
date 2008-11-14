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

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Properties;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-3836">MNG-3836</a>.
 * 
 * @author Benjamin Bentmann
 * @version $Id$
 */
public class MavenITmng3836PluginConfigInheritanceTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng3836PluginConfigInheritanceTest()
    {
    }

    /**
     * Verify that sub modules can *override* inherited plugin configuration.
     */
    public void testitMNG3836()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3836" );

        Verifier verifier = new Verifier( new File( testDir, "child" ).getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/plugin-config.properties" );

        assertEquals( "4", props.getProperty( "stringParams" ) );
        assertEquals( "PASSED-1", props.getProperty( "stringParams.0" ) );
        assertEquals( "PASSED-3", props.getProperty( "stringParams.1" ) );
        assertEquals( "PASSED-2", props.getProperty( "stringParams.2" ) );
        assertEquals( "PASSED-4", props.getProperty( "stringParams.3" ) );

        assertEquals( "4", props.getProperty( "listParam" ) );
        assertEquals( "PASSED-1", props.getProperty( "listParam.0" ) );
        assertEquals( "PASSED-3", props.getProperty( "listParam.1" ) );
        assertEquals( "PASSED-2", props.getProperty( "listParam.2" ) );
        assertEquals( "PASSED-4", props.getProperty( "listParam.3" ) );

        assertEquals( "4", props.getProperty( "domParam.children" ) );
        assertEquals( "PASSED-1", props.getProperty( "domParam.children.echo.0.value" ) );
        assertEquals( "PASSED-3", props.getProperty( "domParam.children.echo.1.value" ) );
        assertEquals( "PASSED-2", props.getProperty( "domParam.children.echo.2.value" ) );
        assertEquals( "PASSED-4", props.getProperty( "domParam.children.echo.3.value" ) );
    }

}