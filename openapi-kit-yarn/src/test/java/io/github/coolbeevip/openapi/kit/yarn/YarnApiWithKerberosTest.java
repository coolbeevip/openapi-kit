package io.github.coolbeevip.openapi.kit.yarn;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.coolbeevip.openapi.kit.yarn.api.ApiClient;
import io.github.coolbeevip.openapi.kit.yarn.api.ApiException;
import io.github.coolbeevip.openapi.kit.yarn.api.YarnApi;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.auth.AuthSchemeFactory;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.KerberosConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.KerberosSchemeFactory;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.config.Lookup;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.http.client.config.AuthSchemes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YarnApiWithKerberosTest {
  static YarnApi api = new YarnApi();
  static ObjectMapper mapper = new ObjectMapper();

  @Test
  void appsGetTest() throws ApiException, JsonProcessingException {
    print(api.wsV1ClusterAppsGet());
  }


  @Test
  void appsGetWithKerberosTest() throws ApiException, JsonProcessingException {
    print(api.wsV1ClusterAppsGet());
  }

  @BeforeAll
  void beforeAll() {
    System.setProperty("java.security.krb5.conf", "krb5.conf");
    System.setProperty("java.security.auth.login.config", "login.conf");
    System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");

    CredentialsProvider credsProvider = new BasicCredentialsProvider();

    KerberosConfig config = KerberosConfig.DEFAULT;
    DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

    Lookup<AuthSchemeFactory> authSchemeRegistry = RegistryBuilder.<AuthSchemeFactory>create()
        .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory(config, dnsResolver)).build();

    CloseableHttpClient httpclient = HttpClients.custom().setDefaultAuthSchemeRegistry(authSchemeRegistry)
        .setDefaultCredentialsProvider(credsProvider).build();

    ApiClient apiClient = new ApiClient(httpclient);
    apiClient.setBasePath("http://10.19.36.211:27656");
    api.setApiClient(apiClient);
  }

  void print(Object bean) throws JsonProcessingException {
    System.out.println(mapper.writeValueAsString(bean));
  }
}
