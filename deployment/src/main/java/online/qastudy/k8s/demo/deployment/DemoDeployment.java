package online.qastudy.k8s.demo.deployment;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.openshift.api.model.*;
import io.fabric8.openshift.client.OpenShiftClient;

import java.text.Collator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DemoDeployment {

    private DefaultKubernetesClient kubernetesClient;
    private Config config;
    private OpenShiftClient ocClient;
    private ProjectRequest request;


    public DemoDeployment(String namespace) {
        this.config = new ConfigBuilder()
                .withMasterUrl("https://127.0.0.1:8443")
                .withUsername("developer")
                .withPassword("developer")
                .withNamespace(namespace)
                .withTrustCerts(true)
                .build();
        this.kubernetesClient = new DefaultKubernetesClient(config);
    }

    //    oc login -u developer -p developer
    public DemoDeployment login() {
        ocClient = kubernetesClient.adapt(OpenShiftClient.class);
        return this;
    }

    public DemoDeployment createNewProject(String projectName, String displayName, String description) {
        request = ocClient.projectrequests().createNew()
                .withNewMetadata()
                .withName(projectName)
                .endMetadata()
                .withDescription(description)
                .withDisplayName(displayName)
                .done();

        System.out.println("Project with name was created: " + request.getMetadata().getName());
        return this;
    }

    // oc new-app yemax/pod-demo:1 --name=pod-demo
    public DemoDeployment deployPod() {
        String namespace = ocClient.getNamespace();
        System.out.println("Namespace is: " + namespace);
        ServiceAccount podDemo = new ServiceAccountBuilder().withNewMetadata().withName("pod-demo").endMetadata().build();

        Map<String, String> podSelectors = new HashMap<>();
        podSelectors.put("app", "pod-demo");
        podSelectors.put("deploymentconfig", "pod-demo");


        ocClient.deploymentConfigs().inNamespace(namespace).createOrReplaceWithNew()
                .withNewMetadata()
                .withLabels((Map<String, String>) new HashMap<>().put("app", "pod-name"))
                .withName("pod-demo")
                .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withRevisionHistoryLimit(10)
                .withSelector(podSelectors)
                .addNewTrigger()
                .withType("ConfigChange")
                .endTrigger()
                .addToSelector("app", "pod-demo")
                .withNewTemplate()
                .withNewMetadata()
                .withLabels(podSelectors)
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("pod-demo")
                .withImage("yemax/pod-demo:1")
                .addNewPort()
                .withContainerPort(8081)
                .withProtocol("TCP")
                .endPort()
                .endContainer()
                .withDnsPolicy("ClusterFirst")
                .withRestartPolicy("Always")
                .endSpec()
                .endTemplate()
                .addNewTrigger()
                .withType("ConfigChange")
                .endTrigger()
                .endSpec()
                .withNewStatus()
                .withAvailableReplicas(1)
                .withConditions(
                        new DeploymentConditionBuilder().withStatus("True").withType("Admitted").build(),
                        new DeploymentConditionBuilder().withStatus("True").withType("Progressing").build()
                        )
                .endStatus()
                .done();
        return this;
    }

    // oc expose svc/pod-demo-oc
    public DemoDeployment deployService() {
        Map<String, String> serviceSelector = new HashMap<>();
        serviceSelector.put("app", "pod-demo");
        serviceSelector.put("deploymentconfig", "pod-demo");

        ocClient.services().createOrReplaceWithNew()
                .withNewMetadata()
                .withLabels((Map<String, String>) new HashMap<>().put("app", "pod-name"))
                .withName("pod-demo")
                .withNamespace(ocClient.getNamespace())
                .endMetadata()
                .withNewSpec()
                .withClusterIP("172.30.152.124")
                .withPorts(new ServicePortBuilder()
                        .withName("8081-tcp")
                        .withPort(8081)
                        .withProtocol("TCP")
                        .withNewTargetPort()
                        .withIntVal(8081)
                        .endTargetPort().build())
                .withSelector(serviceSelector)
                .withType("ClusterIP").endSpec().done();
        return this;
    }

    public DemoDeployment createRout() {
        ocClient.routes().createOrReplaceWithNew()
                .withNewMetadata()
                .withLabels((Map<String, String>) new HashMap<>().put("app", "pod-demo"))
                .withName("pod-demo")
                .withNamespace("qa-fest-2018")
                .endMetadata()
                .withNewSpec()
                .withHost("pod-demo-qa-fest-2018.127.0.0.1.nip.io")
                .withNewPort()
                .withNewTargetPort()
                .withIntVal(8081)
                .endTargetPort()
                .endPort()
                .withNewTo()
                .withKind("Service")
                .withName("pod-demo")
                .withWeight(100)
                .endTo()
                .withWildcardPolicy("None")
                .endSpec()
                .withNewStatus()
                .addNewIngress()
                .withConditions(new RouteIngressConditionBuilder().withStatus("True").withType("Admitted").build())
                .withHost("pod-demo-qa-fest-2018.127.0.0.1.nip.io")
                .withRouterName("router")
                .withWildcardPolicy("None")
                .endIngress()
                .endStatus()
                .done();
        return this;
    }

    public String getApplicationURL() {
        return "http://"+ocClient.routes().list().getItems().stream()
                .filter(route -> route.getMetadata().getName().toLowerCase().equals("pod-demo"))
                .collect(Collectors.toList())
                .get(0).getSpec().getHost();
    }

    public void close() {
        ocClient.close();
    }
}
