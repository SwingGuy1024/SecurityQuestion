package org.question.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 2/5/21
 * <p>Time: 1:57 AM
 *
 * @author Miguel Mu\u00f1oz
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-01-08T23:22:44.934923-08:00[America/Los_Angeles]")
@RestController
@RequestMapping("${openapi.customerOrders.base-path:}")
public class CustomRestController {

  private static final Logger log = LoggerFactory.getLogger(CustomRestController.class);

  @Autowired
  public CustomRestController(
  ) {
    log.trace("instantiating AdminApiController");
  }

  @GetMapping(value = "/view/methodOne/", produces = {"application/text"})
  public ResponseEntity<String> methodOne() {
    return new ResponseEntity<>("MethodOne", HttpStatus.OK);
  }

  @GetMapping(value = "/admin/methodTwo/", produces = {"application/text"})
  public ResponseEntity<String> methodTwo() {
    return new ResponseEntity<>("MethodTwo", HttpStatus.OK);
  }

  @GetMapping(value = "/admin/methodThree/", produces = {"application/text"})
  public ResponseEntity<String> methodThree() {
    return new ResponseEntity<>("MethodThree", HttpStatus.OK);
  }

  @GetMapping(value = "/admin/failureModeOne/", produces = {"application/text"})
  public ResponseEntity<String> failureModeOne() {
    return new ResponseEntity<>("failureModeOne", HttpStatus.OK);
  }

  @GetMapping(value = "/admin/failureModeThree/", produces = {"application/text"})
  public ResponseEntity<String> failureModeThree() {
    return new ResponseEntity<>("failureModeThree", HttpStatus.OK);
  }

  @GetMapping(value = "/admin/failureModeFour/", produces = {"application/text"})
  public ResponseEntity<String> failureModeFour() {
    return new ResponseEntity<>("failureModeFour", HttpStatus.OK);
  }
}