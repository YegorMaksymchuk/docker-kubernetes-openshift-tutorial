package online.qastudy.k8s.demo.deployment;


import org.junit.Before;
import org.junit.Test;

public class DemoDeploymentTest {
    DemoDeployment demoDeployment;

    @Before
    public void deploy() {
        demoDeployment = new DemoDeployment("qa-fest-2018");

    }

    @Test
    public void testAppDeployment() {
        demoDeployment.login()
                .createNewProject("qa-fest-2018", "Demo for QA Fest 2018", "Demo of Fabric8")
                .deployAndRunPod().createService().createRout()
                .close();
    }
}
