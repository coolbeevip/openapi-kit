package io.github.coolbeevip.openapi.kit.openstack;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.coolbeevip.openapi.kit.openstack.api.ApiException;
import io.github.coolbeevip.openapi.kit.openstack.api.OpenstackApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenStackApiTest {
  static OpenstackApi api = new OpenstackApi();
  static ObjectMapper mapper = new ObjectMapper();

  @Test
  void appsGetTest() throws ApiException, JsonProcessingException {
    print(api.rootGet());
  }

  @BeforeAll
  void beforeAll() {
    api.getApiClient().setBasePath("http://10.19.36.211:27656");
  }

  void print(Object bean) throws JsonProcessingException {
    System.out.println(mapper.writeValueAsString(bean));
  }
}
