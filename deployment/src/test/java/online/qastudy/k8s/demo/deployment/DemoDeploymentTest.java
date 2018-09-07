package online.qastudy.k8s.demo.deployment;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import static org.assertj.core.api.Assertions.assertThat;

public class DemoDeploymentTest {
    DemoDeployment demoDeployment;

    @Before
    public void deploy() {demoDeployment = new DemoDeployment("qa-fest-2018");}

    @After
    public void cleanup(){demoDeployment.close();}

    @Test
    public void testAppDeployment() {
        assertThat(demoDeployment.login()
                    .createNewProject("qa-fest-2018", "Demo for QA Fest 2018", "Demo of Fabric8")
                    .deployPod()
                    .deployService()
                    .createRout()
                    .getApplicationURL()).contains("qa-fest-2018").contains("pod-demo");
    }



}
