// package net.flyingfishflash.ledger.integration.core.users;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// import net.flyingfishflash.ledger.core.response.structure.Response;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
//
// import net.flyingfishflash.ledger.core.users.data.dto.UserProfileResponse;
// import net.flyingfishflash.ledger.core.users.service.UserService;
//
// import java.net.URI;
//
// @SpringBootTest(
//    webEnvironment = WebEnvironment.RANDOM_PORT,
//    properties = {"spring.main.allow-bean-definition-overriding=true"})
//// @AutoConfigureMockMvc
// class UserControllerTests {
//
//  @Value("${config.application.api-v1-url-path}")
//  private String apiPrefix;
//
//  // @Autowired private MockMvc mvc;
//  @Autowired private TestRestTemplate restTemplate;
//  @Autowired UserService userService;
//
//  @Test
//  void profileByUsername() {
//
//    ParameterizedTypeReference<Response<UserProfileResponse>> responseType =
//            new ParameterizedTypeReference<>() {};
//
//    ResponseEntity<Response<UserProfileResponse>> userProfileResponse =
//        restTemplate
//            //.withBasicAuth("testuser", "TestUser1@")
//                .exchange(URI.create(String.format("%s/users/profile", apiPrefix)),
// HttpMethod.GET, ResponseEntity<String>), responseType;
//    assertThat(userProfileResponse).hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK);
//  }
//
//  //  @Test
//  //  @WithMockUser("testuser")
//  //  void getEmployeeByIdAPI() throws Exception {
//  //    mvc.perform(MockMvcRequestBuilders.get(String.format("%s/users/profile", apiPrefix),
//  // 1).accept(MediaType.APPLICATION_JSON))
//  //        .andDo(throw new IllegalStateException("ddd");)
//  //        .andExpect(status().isOk())
//  //        .andExpect(MockMvcResultMatchers.jsonPath("$.employeeId").value(1));
//  //  }
// }
