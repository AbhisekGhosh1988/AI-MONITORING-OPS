package com.ai.ops.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;

@Configuration
public class KubernetesConfig {

    @Bean
    public ApiClient apiClient() throws Exception {

        String kubeConfigPath = System.getProperty("user.home") + "/.kube/config";

        ApiClient client = ClientBuilder.kubeconfig
                (KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);

        return client;
    }
}
