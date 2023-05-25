package io.github.coolbeevip.openapi.kit.flink;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.coolbeevip.openapi.kit.flink.api.ApiException;
import io.github.coolbeevip.openapi.kit.flink.api.FlinkApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlinkApiTest {
  static FlinkApi api = new FlinkApi();
  static ObjectMapper mapper = new ObjectMapper();

  String taskManagersMetricsGet;
  String jobManagersMetricsGet;
  String jobsMetricsGet;
  List<String> verticesMetricsGet = Arrays.asList("numRecordsIn", "numRecordsInPerSecond", "numRecordsOut");

  @Test
  void configGetTest() throws ApiException, JsonProcessingException {
    print(api.configGet());
  }

  @Test
  void overviewGetTest() throws ApiException, JsonProcessingException {
    print(api.overviewGet());
  }

  @Test
  void datasetsGetTest() throws ApiException, JsonProcessingException {
    print(api.datasetsGet());
  }

  @Test
  void jarsGetTest() throws ApiException, JsonProcessingException {
    print(api.jarsGet());
  }

  @Test
  void jobmanagerConfigGetTest() throws ApiException, JsonProcessingException {
    print(api.jobmanagerConfigGet());
  }

  @Test
  void jobmanagerLogsGetTest() throws ApiException, JsonProcessingException {
    print(api.jobmanagerLogsGet());
  }

  @Test
  void jobmanagerMetricsGetTest() throws ApiException, JsonProcessingException {
    print(api.jobmanagerMetricsGet(jobManagersMetricsGet));
  }

  @Test
  void taskmanagersGetTest() throws ApiException, JsonProcessingException {
    print(api.taskmanagersGet());
  }

  @Test
  void taskmanagersTaskmanageridMetricsGetTest() throws ApiException {
    api.taskmanagersGet().getTaskmanagers().forEach(task -> {
      try {
        print(api.taskmanagersTaskmanageridMetricsGet(task.getId(), taskManagersMetricsGet));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Test
  void taskmanagersMetricsGetTest() throws ApiException, JsonProcessingException {
    print(api.taskmanagersMetricsGet(taskManagersMetricsGet, "min,max,avg,sum", null));
  }

  @Test
  void jobsGetTest() throws ApiException, JsonProcessingException {
    print(api.jobsGet());
  }

  @Test
  void jobsJobidGetTest() throws ApiException {
    api.jobsGet().getJobs().forEach(jobIdWithStatus -> {
      try {
        print(api.jobsJobidGet(jobIdWithStatus.getId()));
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Test
  void jobsJobidCheckpointsGetTest() throws ApiException {
    api.jobsGet().getJobs().forEach(jobIdWithStatus -> {
      try {
        print(api.jobsJobidCheckpointsGet(jobIdWithStatus.getId()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Test
  void jobsMetricsGetTest() throws ApiException {
    api.jobsGet().getJobs().forEach(jobIdWithStatus -> {
      try {
        print(api.jobsMetricsGet(jobsMetricsGet, null, null));
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Test
  void jobsJobidMetricsGetTest() throws ApiException {
    api.jobsGet().getJobs().forEach(jobIdWithStatus -> {
      try {
        print(api.jobsJobidMetricsGet(jobIdWithStatus.getId(), jobsMetricsGet));
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Test
  void jobsJobidVerticesVertexidGetTest() throws ApiException {
    api.jobsGet().getJobs().forEach(jobIdWithStatus -> {
      try {
        api.jobsJobidGet(jobIdWithStatus.getId()).getVertices().forEach(jobVertexDetailsInfo -> {
          try {
            print(api.jobsJobidVerticesVertexidGet(jobIdWithStatus.getId(), jobVertexDetailsInfo.getId()));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }
    });
  }

  /**
   * /jobs/:jobid/vertices/:vertexid/metrics
   */
  @Test
  void jobsJobidVerticesVertexidMetricsGet() throws ApiException {
    api.jobsGet().getJobs().forEach(jobIdWithStatus -> {
      try {
        api.jobsJobidGet(jobIdWithStatus.getId()).getVertices().forEach(jobVertexDetailsInfo -> {
          try {
            List<String> get = new ArrayList<>();
            verticesMetricsGet.forEach(metric -> {
              for (int i = 0; i < jobVertexDetailsInfo.getParallelism(); i++) {
                get.add(i + "." + metric);
              }
            });
            print(api.jobsJobidVerticesVertexidMetricsGet(jobIdWithStatus.getId(), jobVertexDetailsInfo.getId(),
                get.stream().collect(Collectors.joining(","))));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @BeforeAll
  void beforeAll() throws ApiException {
    api.getApiClient().setBasePath("http://oss-ndcp-211:27656/proxy/application_1684222589848_0011");
    //api.getApiClient().setBasePath("http://10.19.36.213:8081");
    taskManagersMetricsGet =
        api.taskmanagersMetricsGet(null, null, null).stream().map(m -> m.getId()).collect(Collectors.joining(","));
    jobManagersMetricsGet = api.jobmanagerMetricsGet(null).stream().map(m -> m.getId()).collect(Collectors.joining(","
    ));
    jobsMetricsGet = api.jobsMetricsGet(null, null, null).stream().map(m -> m.getId()).collect(Collectors.joining(","));
  }

  void print(Object bean) throws JsonProcessingException {
    mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    System.out.println(mapper.writeValueAsString(bean));
  }
}
