package io.github.coolbeevip.openapi.kit.yarn;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.coolbeevip.openapi.kit.yarn.api.ApiException;
import io.github.coolbeevip.openapi.kit.yarn.api.YarnApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YarnApiTest {
  static YarnApi api = new YarnApi();
  static ObjectMapper mapper = new ObjectMapper();

  @Test
  void appsGetTest() throws ApiException, JsonProcessingException {
    print(api.wsV1ClusterAppsGet());
  }

  @BeforeAll
  void beforeAll() {
    api.getApiClient().setBasePath("http://10.19.36.211:27656");
  }

  void print(Object bean) throws JsonProcessingException {
    System.out.println(mapper.writeValueAsString(bean));
  }
}
