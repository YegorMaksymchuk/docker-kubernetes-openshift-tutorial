package online.qastudy.k8s.demo.deployment;


import org.junit.Test;

public class DemoDeploymentTest {

    @Test
    public void testAppDeployment(){
        DemoDeployment demoDeployment = new DemoDeployment();
        demoDeployment.login()
                .createNewProject("test-demo","Demo for QA Fest 2018","Demo of Fabric8")
                .close();
    }
}
