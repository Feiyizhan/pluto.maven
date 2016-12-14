package demo.pluto.maven;

import demo.pluto.maven.email.EmailDemo;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
//    public void testApp()
//    {
//        assertTrue( true );
//    }
//    public void testSendSimpleMailMessage(){
//        EmailDemo email = new EmailDemo();
//        email.sendSimpleMailMessage();
//    }
    
//    public void testSendHtmlMailMessage(){
//        EmailDemo email = new EmailDemo();
//        email.sendHtmlMailMessage();
//    }
    
    public void testSendTemplateMail(){
        EmailDemo email = new EmailDemo();
        email.sendTemplateMail();
    }
}
