package org.question.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/5/21
 * <p>Time: 1:57 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-01-08T23:22:44.934923-08:00[America/Los_Angeles]")
@Controller
@RequestMapping("${openapi.customerOrders.base-path:}")
public class RestController {

  private static final Logger log = LoggerFactory.getLogger(RestController.class);

//  private final NativeWebRequest request;
//
//  private final ObjectMapper objectMapper;

  @Autowired
  public RestController(
//      final NativeWebRequest request,
//      final ObjectMapper objectMapper
  ) {
//    this.request = request;
//    this.objectMapper = objectMapper;
    log.trace("instantiating AdminApiController");
  }

  @GetMapping(value = "/admin/failureOne/", produces = {"application/text"})
  public ResponseEntity<String> methodOne() {
    return new ResponseEntity<>("MethodOne", HttpStatus.OK);
  }

  @GetMapping(value = "/admin/failureTwo/", produces = {"application/text"})
  public ResponseEntity<String> methodTwo() {
    return new ResponseEntity<>("MethodTwo", HttpStatus.OK);
  }

  @GetMapping(value = "/view/methodThree/", produces = {"application/text"})
  public ResponseEntity<String> methodThree() {
    return new ResponseEntity<>("MethodThree", HttpStatus.OK);
  }

  @GetMapping(value = "/admin/methodFour/", produces = {"application/text"})
  public ResponseEntity<String> methodFour() {
    return new ResponseEntity<>("MethodFour", HttpStatus.OK);
  }

  @GetMapping(value = "/admin/methodFive/", produces = {"application/text"})
  public ResponseEntity<String> methodFive() {
    return new ResponseEntity<>("MethodFive", HttpStatus.OK);
  }
}