package online.qastudy.k8s.demo.deployment;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(JUnitPlatform.class)
public class DemoDeploymentTest {
    private DemoDeployment demoDeployment;

    @Before
    public void deploy() {demoDeployment = new DemoDeployment("qmo-2018");}

    @After
    public void cleanup(){demoDeployment.close();}

    @Test
    public void testAppDeployment() {
        assertThat(demoDeployment.login()
                    .createNewProject("qmo-2018", "Demo for QMO 2018", "Demo of Fabric8")
                    .deployPod()
                    .deployService()
                    .createRout()
                    .getApplicationURL()).contains("qmo-2018").contains("pod-demo");
    }
}
