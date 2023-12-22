package se.digg.api.endtoend;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@SelectClasses({RegisterOrganisationEndToEndTest.class, ImpressionEndToEndTest.class, RatingEndToEndTest.class})
@Suite
public class ApplicationEndToEndTestSuite {
}
