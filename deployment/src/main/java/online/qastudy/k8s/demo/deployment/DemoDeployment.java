package online.qastudy.k8s.demo.deployment;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.openshift.api.model.*;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class DemoDeployment {

    DefaultKubernetesClient kubernetesClient;
    Config config;
    OpenShiftClient openShiftClient;

    public DemoDeployment() {
        this.config = new ConfigBuilder().withMasterUrl("https://127.0.0.1:8443").withUsername("developer").withPassword("developer").build();
        this.kubernetesClient = new DefaultKubernetesClient(config);
    }

    //    oc login -u developer -p developer
    public DemoDeployment login() {
        openShiftClient = kubernetesClient.adapt(OpenShiftClient.class);
        return this;
    }

    public DemoDeployment createNewProject(String projectName, String displayName, String description) {
        ProjectRequest request = openShiftClient.projectrequests().createNew()
                .withNewMetadata()
                .withName(projectName)
                .endMetadata()
                .withDescription(description)
                .withDisplayName(displayName)
                .done();
        return this;
    }

    // oc new-app yemax/pod-demo:1 --name=pod-demo
    public DemoDeployment createPod() {

        return this;
    }

    // oc expose svc/pod-demo-oc
    public DemoDeployment createService(){

        return this;
    }
        public void close () {
            openShiftClient.close();
        }
    }
