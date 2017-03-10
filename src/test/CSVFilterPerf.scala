package com.ons.gov.uk.backend.test


class CSVFilterPerf extends Simulation {
  val httpProtocol = http
    .baseURL("http://computer-database.gatling.io")
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-GB,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:51.0) Gecko/20100101 Firefox/51.0")

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")


  val scn = scenario("RecordedSimulation")
    .exec(http("request_0")
      .get("/")
      .headers(headers_0)
      .resources(http("request_1")
        .get("/favicon.ico")
        .check(status.is(404))))
    .pause(10)
    .exec(http("request_2")
      .get("/computers?f=mac")
      .headers(headers_0))
    .pause(4)
    .exec(http("request_3")
      .get("/computers/224")
      .headers(headers_0))
    .pause(10)
    .exec(http("request_4")
      .post("/computers/224")
      .headers(headers_0)
      .formParam("name", "IMac G3")
      .formParam("introduced", "")
      .formParam("discontinued", "")
      .formParam("company", "1"))

  setUp(scn.inject(atOnceUsers(100))).protocols(httpProtocol)

}
