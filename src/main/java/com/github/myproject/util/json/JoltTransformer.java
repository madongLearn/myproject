package com.github.myproject.util.json;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Auther: madong
 * @Date: 19-7-24 18:05
 * @Description:
 */
public class JoltTransformer {

    public static final Logger logger = LoggerFactory.getLogger(JoltTransformer.class);


    public static String transform(String inParams, String rule) {
        if (StringUtils.isEmpty(rule) || StringUtils.isEmpty(inParams)) {
            return inParams;
        }
        List chainrSpecJSON = JsonUtils.jsonToList(rule);
        Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
        Object inputJSON = JsonUtils.jsonToObject(inParams, "utf-8");
        Object transformedOutput = chainr.transform(inputJSON);
        return JsonUtils.toJsonString(transformedOutput);
    }


    /*public static void main(String[] args) {
        String s= "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"a\": \"11\",\n" +
                "      \"b\": [\n" +
                "        {\n" +
                "          \"c\": \"1\",\n" +
                "          \"d\": \"11\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"c\": \"2\",\n" +
                "          \"d\": \"22\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"a\": \"12\",\n" +
                "      \"b\": [\n" +
                "        {\n" +
                "          \"c\": \"3\",\n" +
                "          \"d\": \"33\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"c\": \"4\",\n" +
                "          \"d\": \"44\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String rule ="[\n" +
                "  {\n" +
                "    \"operation\": \"shift\",\n" +
                "    \"spec\": {\n" +
                "      \"data\": {\n" +
                "        \"*\": {\n" +
                "          \"a\": \"data[&1].aa\",\n" +
                "          \"b\": {\n" +
                "            \"*\": {\n" +
                "              \"c\":\"data[&3].bb[&1].cc\",\n" +
                "              \"d\":\"data[&3].bb[&1].dd\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "]";
                *//*"[\n" +
                "  {\n" +
                "    \"operation\": \"shift\",\n" +
                "    \"spec\": {\n" +
                "      \"data\": {\n" +
                "        \"*\": {\n" +
                "          \"a\": \"data[&1].aa\",\n" +
                "          \"b\": \"data[&1].bb\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "]"*//*;
        System.out.println(transform(s, rule));
    }*/
    public static void main(String[] args) {
        String s= null;
        String rule ="[{\n" +
                "  \"operation\": \"shift\",\n" +
                "  \"spec\": {\n" +
                "    \"header\": {\n" +
                "      \"systemCode\": \"india\\\\.credit\\\\.allinfo.header.systemCode\",\n" +
                "      \"messageText\": \"india\\\\.credit\\\\.allinfo.header.messageText\",\n" +
                "      \"reportDate\": \"india\\\\.credit\\\\.allinfo.header.header_reportDate\",\n" +
                "      \"reportTime\": \"india\\\\.credit\\\\.allinfo.header.header_reportTime\"\n" +
                "    },\n" +
                "    \"userMessage\": {\n" +
                "      \"@\": \"india\\\\.credit\\\\.allinfo.&0\"\n" +
                "    },\n" +
                "    \"creditProfileHeader\": {\n" +
                "      \"enquiryUsername\": \"india\\\\.credit\\\\.allinfo.creditProfileHeader.systemCode\",\n" +
                "      \"reportDate\": \"india\\\\.credit\\\\.allinfo.creditProfileHeader.creditProfileHeader_reportDate\",\n" +
                "      \"reportTime\": \"india\\\\.credit\\\\.allinfo.creditProfileHeader.creditProfileHeader_reportTime\",\n" +
                "      \"version\": \"india\\\\.credit\\\\.allinfo.creditProfileHeader.version\",\n" +
                "      \"reportNumber\": \"india\\\\.credit\\\\.allinfo.creditProfileHeader.reportNumber\",\n" +
                "      \"subscriber\": \"india\\\\.credit\\\\.allinfo.creditProfileHeader.subscriber\",\n" +
                "      \"subscriberName\": \"india\\\\.credit\\\\.allinfo.creditProfileHeader.subscriberName\"\n" +
                "    },\n" +
                "    \"matchResult\": {\n" +
                "      \"@\": \"india\\\\.credit\\\\.allinfo.&0\"\n" +
                "    },\n" +
                "    \"totalcapsSummary\": {\n" +
                "      \"@\": \"india\\\\.credit\\\\.allinfo.&0\"\n" +
                "    },\n" +
                "    \"segment\": {\n" +
                "      \"@\": \"india\\\\.credit\\\\.allinfo.&0\"\n" +
                "    },\n" +
                "    \"score\": {\n" +
                "      \"@\": \"india\\\\.credit\\\\.allinfo.&0\"\n" +
                "    },\n" +
                "    \"nonCreditCAPS\": {\n" +
                "      \"noncreditcapsSummary\": {\n" +
                "        \"@\": \"india\\\\.credit\\\\.noncapsinfo.&0\"\n" +
                "      },\n" +
                "      \"capsApplicationDetails\": {\n" +
                "        \"*\": {\n" +
                "          \"subscriberCode\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].subscriberCode\",\n" +
                "          \"subscriberName\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].subscriberName\",\n" +
                "          \"dateOfRequest\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].dateOfRequest\",\n" +
                "          \"reportTime\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].reportTime\",\n" +
                "          \"reportNumber\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].reportNumber\",\n" +
                "          \"enquiryReason\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].enquiryReason\",\n" +
                "          \"financePurpose\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].financePurpose\",\n" +
                "          \"amountFinanced\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].amountFinanced\",\n" +
                "          \"durationOfAgreement\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&1].durationOfAgreement\",\n" +
                "          \"capsApplicantDetails\": {\n" +
                "            \"lastName\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.lastName\",\n" +
                "            \"firstName\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.firstName\",\n" +
                "            \"middleName1\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.middleName1\",\n" +
                "            \"middleName2\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.middleName2\",\n" +
                "            \"middleName3\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.middleName3\",\n" +
                "            \"genderCode\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.genderCode\",\n" +
                "            \"incomeTaxPan\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.incomeTaxPan\",\n" +
                "            \"panIssueDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.panIssueDate\",\n" +
                "            \"panExpirationDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.panExpirationDate\",\n" +
                "            \"passportNumber\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.passportNumber\",\n" +
                "            \"passportIssueDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.passportIssueDate\",\n" +
                "            \"passportExpirationDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.passportExpirationDate\",\n" +
                "            \"voterSIdentityCard\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.voterSIdentityCard\",\n" +
                "            \"voterIdIssueDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.voterIDIssueDate\",\n" +
                "            \"voterIdExpirationDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.voterIDExpirationDate\",\n" +
                "            \"driverLicenseNumber\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.driverLicenseNumber\",\n" +
                "            \"driverLicenseIssueDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.driverLicenseIssueDate\",\n" +
                "            \"driverLicenseExpirationDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.driverLicenseExpirationDate\",\n" +
                "            \"rationCardNumber\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.rationCardNumber\",\n" +
                "            \"rationCardIssueDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.rationCardIssueDate\",\n" +
                "            \"rationCardExpirationDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.rationCardExpirationDate\",\n" +
                "            \"universalIdNumber\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.universalIDNumber\",\n" +
                "            \"universalIdIssueDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.universalIDIssueDate\",\n" +
                "            \"universalIdExpirationDate\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.universalIDExpirationDate\",\n" +
                "            \"dateOfBirthApplicant\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.dateOfBirthApplicant\",\n" +
                "            \"telephoneNumberApplicant1st\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.telephoneNumberApplicant1St\",\n" +
                "            \"telephoneType\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.telephoneType\",\n" +
                "            \"telephoneExtension\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.telephoneExtension\",\n" +
                "            \"mobilePhoneNumber\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.mobilePhoneNumber\",\n" +
                "            \"emailId\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantDetails.eMailId\"\n" +
                "          },\n" +
                "          \"capsOtherDetails\": {\n" +
                "            \"@\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].&0\"\n" +
                "          },\n" +
                "          \"capsApplicantAddressDetails\": {\n" +
                "            \"flatNoPlotNoHouseNo\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_flatNoPlotNoHouseNo\",\n" +
                "            \"bldgNoSocietyName\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_bldgNoSocietyName\",\n" +
                "            \"roadNoNameAreaLocality\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_roadNoNameAreaLocality\",\n" +
                "            \"city\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_city\",\n" +
                "            \"landmark\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_landmark\",\n" +
                "            \"state\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_state\",\n" +
                "            \"pincode\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_pinCode\",\n" +
                "            \"countryCode\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_countryCode\"\n" +
                "          },\n" +
                "          \"capsApplicantAdditionalAddressDetails\": {\n" +
                "            \"flatNoPlotNoHouseNo\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_flatNoPlotNoHouseNo\",\n" +
                "            \"bldgNoSocietyName\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_bldgNoSocietyName\",\n" +
                "            \"roadNoNameAreaLocality\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_roadNoNameAreaLocality\",\n" +
                "            \"city\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_city\",\n" +
                "            \"landmark\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_landmark\",\n" +
                "            \"state\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_state\",\n" +
                "            \"pincode\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_pinCode\",\n" +
                "            \"countryCode\": \"india\\\\.credit\\\\.noncapsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_countryCode\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"currentApplication\": {\n" +
                "      \"currentApplicationDetails\": {\n" +
                "        \"enquiryReason\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.enquiryReason\",\n" +
                "        \"financePurpose\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.financePurpose\",\n" +
                "        \"amountFinanced\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.amountFinanced\",\n" +
                "        \"durationOfAgreement\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.durationOfAgreement\",\n" +
                "        \"currentApplicantDetails\": {\n" +
                "          \"lastName\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.lastName\",\n" +
                "          \"firstName\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.firstName\",\n" +
                "          \"middleName1\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.middleName1\",\n" +
                "          \"middleName2\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.middleName2\",\n" +
                "          \"middleName3\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.middleName3\",\n" +
                "          \"genderCode\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.genderCode\",\n" +
                "          \"incomeTaxPan\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.incomeTaxPan\",\n" +
                "          \"panIssueDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.panIssueDate\",\n" +
                "          \"panExpirationDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.panExpirationDate\",\n" +
                "          \"passportNumber\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.passportNumber\",\n" +
                "          \"passportIssueDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.passportIssueDate\",\n" +
                "          \"passportExpirationDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.passportExpirationDate\",\n" +
                "          \"voterSIdentityCard\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.voterSIdentityCard\",\n" +
                "          \"voterIdIssueDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.voterIDIssueDate\",\n" +
                "          \"voterIdExpirationDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.voterIDExpirationDate\",\n" +
                "          \"driverLicenseNumber\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.driverLicenseNumber\",\n" +
                "          \"driverLicenseIssueDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.driverLicenseIssueDate\",\n" +
                "          \"driverLicenseExpirationDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.driverLicenseExpirationDate\",\n" +
                "          \"rationCardNumber\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.rationCardNumber\",\n" +
                "          \"rationCardIssueDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.rationCardIssueDate\",\n" +
                "          \"rationCardExpirationDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.rationCardExpirationDate\",\n" +
                "          \"universalIdNumber\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.universalIDNumber\",\n" +
                "          \"universalIdIssueDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.universalIDIssueDate\",\n" +
                "          \"universalIdExpirationDate\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.universalIDExpirationDate\",\n" +
                "          \"dateOfBirthApplicant\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.dateOfBirthApplicant\",\n" +
                "          \"telephoneNumberApplicant1st\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.telephoneNumberApplicant1St\",\n" +
                "          \"telephoneExtension\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.telephoneExtension\",\n" +
                "          \"telephoneType\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.telephoneType\",\n" +
                "          \"mobilePhoneNumber\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.mobilePhoneNumber\",\n" +
                "          \"emailId\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantDetails.eMailId\"\n" +
                "        },\n" +
                "        \"currentOtherDetails\": {\n" +
                "          \"@\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.&0\"\n" +
                "        },\n" +
                "        \"currentApplicantAddressDetails\": {\n" +
                "          \"flatNoPlotNoHouseNo\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAddressDetails.currentApplicantAddressDetails_flatNoPlotNoHouseNo\",\n" +
                "          \"bldgNoSocietyName\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAddressDetails.currentApplicantAddressDetails_bldgNoSocietyName\",\n" +
                "          \"roadNoNameAreaLocality\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAddressDetails.currentApplicantAddressDetails_roadNoNameAreaLocality\",\n" +
                "          \"city\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAddressDetails.currentApplicantAddressDetails_city\",\n" +
                "          \"landmark\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAddressDetails.currentApplicantAddressDetails_landmark\",\n" +
                "          \"state\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAddressDetails.currentApplicantAddressDetails_state\",\n" +
                "          \"pincode\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAddressDetails.currentApplicantAddressDetails_pinCode\",\n" +
                "          \"countryCode\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAddressDetails.currentApplicantAddressDetails_countryCode\"\n" +
                "        },\n" +
                "        \"currentApplicantAdditionalAddressDetails\": {\n" +
                "          \"flatNoPlotNoHouseNo\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAdditionalAddressDetails.currentApplicantAdditionalAddressDetails_flatNoPlotNoHouseNo\",\n" +
                "          \"bldgNoSocietyName\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAdditionalAddressDetails.currentApplicantAdditionalAddressDetails_bldgNoSocietyName\",\n" +
                "          \"roadNoNameAreaLocality\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAdditionalAddressDetails.currentApplicantAdditionalAddressDetails_roadNoNameAreaLocality\",\n" +
                "          \"city\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAdditionalAddressDetails.currentApplicantAdditionalAddressDetails_currentApplicantAdditionalAddressDetails_city\",\n" +
                "          \"landmark\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAdditionalAddressDetails.currentApplicantAdditionalAddressDetails_landmark\",\n" +
                "          \"state\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAdditionalAddressDetails.currentApplicantAdditionalAddressDetails_state\",\n" +
                "          \"pincode\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAdditionalAddressDetails.currentApplicantAdditionalAddressDetails_pinCode\",\n" +
                "          \"countryCode\": \"india\\\\.credit\\\\.currentApplicationinfo.currentApplicationDetails.currentApplicantAdditionalAddressDetails.currentApplicantAdditionalAddressDetails_countryCode\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"caps\": {\n" +
                "      \"capsSummary\": {\n" +
                "        \"@\": \"india\\\\.credit\\\\.capsinfo.&0\"\n" +
                "      },\n" +
                "      \"capsApplicationDetails\": {\n" +
                "        \"*\": {\n" +
                "          \"subscriberCode\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].subscriberCode\",\n" +
                "          \"subscriberName\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].subscriberName\",\n" +
                "          \"dateOfRequest\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].dateOfRequest\",\n" +
                "          \"reportTime\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].reportTime\",\n" +
                "          \"reportNumber\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].reportNumber\",\n" +
                "          \"enquiryReason\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].enquiryReason\",\n" +
                "          \"financePurpose\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].financePurpose\",\n" +
                "          \"amountFinanced\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].amountFinanced\",\n" +
                "          \"durationOfAgreement\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&1].durationOfAgreement\",\n" +
                "          \"capsApplicantDetails\": {\n" +
                "            \"lastName\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.lastName\",\n" +
                "            \"firstName\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.firstName\",\n" +
                "            \"middleName1\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.middleName1\",\n" +
                "            \"middleName2\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.middleName2\",\n" +
                "            \"middleName3\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.middleName3\",\n" +
                "            \"genderCode\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.genderCode\",\n" +
                "            \"incomeTaxPan\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.incomeTaxPan\",\n" +
                "            \"panIssueDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.panIssueDate\",\n" +
                "            \"panExpirationDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.panExpirationDate\",\n" +
                "            \"passportNumber\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.passportNumber\",\n" +
                "            \"passportIssueDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.passportIssueDate\",\n" +
                "            \"passportExpirationDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.passportExpirationDate\",\n" +
                "            \"voterSIdentityCard\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.voterSIdentityCard\",\n" +
                "            \"voterIdIssueDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.voterIDIssueDate\",\n" +
                "            \"voterIdExpirationDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.voterIDExpirationDate\",\n" +
                "            \"driverLicenseNumber\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.driverLicenseNumber\",\n" +
                "            \"driverLicenseIssueDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.driverLicenseIssueDate\",\n" +
                "            \"driverLicenseExpirationDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.driverLicenseExpirationDate\",\n" +
                "            \"rationCardNumber\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.rationCardNumber\",\n" +
                "            \"rationCardIssueDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.rationCardIssueDate\",\n" +
                "            \"rationCardExpirationDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.rationCardExpirationDate\",\n" +
                "            \"universalIdNumber\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.universalIDNumber\",\n" +
                "            \"universalIdIssueDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.universalIDIssueDate\",\n" +
                "            \"universalIdExpirationDate\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.universalIDExpirationDate\",\n" +
                "            \"dateOfBirthApplicant\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.dateOfBirthApplicant\",\n" +
                "            \"telephoneNumberApplicant1st\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.telephoneNumberApplicant1St\",\n" +
                "            \"telephoneType\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.telephoneType\",\n" +
                "            \"telephoneExtension\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.telephoneExtension\",\n" +
                "            \"mobilePhoneNumber\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.mobilePhoneNumber\",\n" +
                "            \"emailId\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantDetails.eMailId\"\n" +
                "          },\n" +
                "          \"capsOtherDetails\": {\n" +
                "            \"@\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].&0\"\n" +
                "          },\n" +
                "          \"capsApplicantAddressDetails\": {\n" +
                "            \"flatNoPlotNoHouseNo\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_flatNoPlotNoHouseNo\",\n" +
                "            \"bldgNoSocietyName\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_bldgNoSocietyName\",\n" +
                "            \"roadNoNameAreaLocality\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_roadNoNameAreaLocality\",\n" +
                "            \"city\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_city\",\n" +
                "            \"landmark\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_landmark\",\n" +
                "            \"state\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_state\",\n" +
                "            \"pincode\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_pinCode\",\n" +
                "            \"countryCode\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAddressDetails.capsApplicantAddressDetails_countryCode\"\n" +
                "          },\n" +
                "          \"capsApplicantAdditionalAddressDetails\": {\n" +
                "            \"flatNoPlotNoHouseNo\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_flatNoPlotNoHouseNo\",\n" +
                "            \"bldgNoSocietyName\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_bldgNoSocietyName\",\n" +
                "            \"roadNoNameAreaLocality\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_roadNoNameAreaLocality\",\n" +
                "            \"city\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_city\",\n" +
                "            \"landmark\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_landmark\",\n" +
                "            \"state\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_state\",\n" +
                "            \"pincode\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_pinCode\",\n" +
                "            \"countryCode\": \"india\\\\.credit\\\\.capsinfo.capsApplicationDetails[&2].capsApplicantAdditionalAddressDetails.capsApplicantAdditionalAddressDetails_countryCode\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"caisAccount\": {\n" +
                "      \"caisSummary\": {\n" +
                "        \"creditAccount\": {\n" +
                "          \"@\": \"india\\\\.credit\\\\.caisAccountinfo.caisSummary.&0\"\n" +
                "        },\n" +
                "        \"totalOutstandingBalance\": {\n" +
                "          \"@\": \"india\\\\.credit\\\\.caisAccountinfo.caisSummary.&0\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"caisAccountDetails\": {\n" +
                "        \"*\": {\n" +
                "          \"identificationNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].identificationNumber\",\n" +
                "          \"subscriberName\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].subscriberName\",\n" +
                "          \"accountNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].accountNumber\",\n" +
                "          \"portfolioType\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].portfolioType\",\n" +
                "          \"accountType\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].accountType\",\n" +
                "          \"openDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].openDate\",\n" +
                "          \"creditLimitAmount\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].creditLimitAmount\",\n" +
                "          \"highestCreditOrOriginalLoanAmount\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].highestCreditOrOriginalLoanAmount\",\n" +
                "          \"termsDuration\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].termsDuration\",\n" +
                "          \"termsFrequency\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].termsFrequency\",\n" +
                "          \"scheduledMonthlyPaymentAmount\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].scheduledMonthlyPaymentAmount\",\n" +
                "          \"accountStatus\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].accountStatus\",\n" +
                "          \"paymentRating\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].paymentRating\",\n" +
                "          \"paymentHistoryProfile\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].paymentHistoryProfile\",\n" +
                "          \"specialComment\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].specialComment\",\n" +
                "          \"currentBalance\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].currentBalance\",\n" +
                "          \"amountPastDue\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].amountPastDue\",\n" +
                "          \"dateReported\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].dateReported\",\n" +
                "          \"dateClosed\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].dateClosed\",\n" +
                "          \"dateOfLastPayment\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].dateOfLastPayment\",\n" +
                "          \"suitFiledWillfulDefaultWrittenOffStatus\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].suitFiledWillfulDefaultWrittenOffStatus\",\n" +
                "          \"writtenOffSettledStatus\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].writtenOffSettledStatus\",\n" +
                "          \"valueOfCreditsLastMonth\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].valueOfCreditsLastMonth\",\n" +
                "          \"occupationCode\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].occupationCode\",\n" +
                "          \"settlementAmount\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].settlementAmount\",\n" +
                "          \"valueOfCollateral\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].valueOfCollateral\",\n" +
                "          \"typeOfCollateral\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].typeOfCollateral\",\n" +
                "          \"writtenOffAmtTotal\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].writtenOffAmtTotal\",\n" +
                "          \"writtenOffAmtPrincipal\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].writtenOffAmtPrincipal\",\n" +
                "          \"rateOfInterest\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].rateOfInterest\",\n" +
                "          \"repaymentTenure\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].repaymentTenure\",\n" +
                "          \"promotionalRateFlag\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].promotionalRateFlag\",\n" +
                "          \"income\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].income\",\n" +
                "          \"incomeIndicator\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].incomeIndicator\",\n" +
                "          \"incomeFrequencyIndicator\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].incomeFrequencyIndicator\",\n" +
                "          \"defaultStatusDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].defaultStatusDate\",\n" +
                "          \"litigationStatusDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].litigationStatusDate\",\n" +
                "          \"writeOffStatusDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].writeOffStatusDate\",\n" +
                "          \"dateOfAddition\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].dateOfAddition\",\n" +
                "          \"currencyCode\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].currencyCode\",\n" +
                "          \"subscriberComments\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].subscriberComments\",\n" +
                "          \"consumerComments\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].consumerComments\",\n" +
                "          \"accountHoldertypeCode\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&1].accountHoldertypeCode\",\n" +
                "          \"caisAccountHistory\": {\n" +
                "            \"*\": {\n" +
                "              \"year\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisAccountHistory[&1].caisAccountHistory_year\",\n" +
                "              \"month\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisAccountHistory[&1].caisAccountHistory_month\",\n" +
                "              \"daysPastDue\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisAccountHistory[&1].daysPastDue\",\n" +
                "              \"assetClassification\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisAccountHistory[&1].assetClassification\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"accountReviewData\": {\n" +
                "            \"*\": {\n" +
                "              \"year\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_year\",\n" +
                "              \"month\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_month\",\n" +
                "              \"accountStatus\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_accountStatus\",\n" +
                "              \"actualPaymentAmount\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].actualPaymentAmount\",\n" +
                "              \"currentBalance\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_currentBalance\",\n" +
                "              \"creditLimitAmount\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_creditLimitAmount\",\n" +
                "              \"amountPastDue\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_amountPastDue\",\n" +
                "              \"paymentRating\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_paymentRating\",\n" +
                "              \"cashLimit\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_cashLimit\",\n" +
                "              \"highestCreditOrOriginalLoanAmount\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].accountReviewData_highestCreditOrOriginalLoanAmount\",\n" +
                "              \"emiAmount\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].accountReviewData[&1].emiAmount\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"caisHolderDetails\": {\n" +
                "            \"*\": {\n" +
                "              \"surnameNonNormalized\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].surnameNonNormalized\",\n" +
                "              \"firstNameNonNormalized\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].firstNameNonNormalized\",\n" +
                "              \"middleName1NonNormalized\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].middleName1NonNormalized\",\n" +
                "              \"middleName2NonNormalized\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].middleName2NonNormalized\",\n" +
                "              \"middleName3NonNormalized\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].middleName3NonNormalized\",\n" +
                "              \"alias\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].alias\",\n" +
                "              \"genderCode\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].genderCode\",\n" +
                "              \"incomeTaxPan\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].caisHolderDetails_incomeTaxPan\",\n" +
                "              \"passportNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].passportNumber\",\n" +
                "              \"voterIdNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].caisHolderDetails_voterIDNumber\",\n" +
                "              \"dateOfBirth\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderDetails[&1].dateOfBirth\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"caisHolderAddressDetails\": { \n" +
                "            \"@\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&2].&0\"          \n" +
                "          },\n" +
                "          \"caisHolderPhoneDetails\": {\n" +
                "            \"*\": {\n" +
                "              \"telephoneNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderPhoneDetails[&1].telephoneNumber\",\n" +
                "              \"telephoneType\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderPhoneDetails[&1].telephoneType\",\n" +
                "              \"telephoneExtension\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderPhoneDetails[&1].telephoneExtension\",\n" +
                "              \"mobileTelephoneNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderPhoneDetails[&1].mobileTelephoneNumber\",\n" +
                "              \"faxNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderPhoneDetails[&1].faxNumber\",\n" +
                "              \"emailId\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderPhoneDetails[&1].eMailId\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"caisHolderIdDetails\": {\n" +
                "            \"*\": {\n" +
                "              \"incomeTaxPan\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].incomeTaxPan\",\n" +
                "              \"panIssueDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].panIssueDate\",\n" +
                "              \"panExpirationDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].panExpirationDate\",\n" +
                "              \"passportNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].passportNumber\",\n" +
                "              \"passportIssueDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].passportIssueDate\",\n" +
                "              \"passportExpirationDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].passportExpirationDate\",\n" +
                "              \"voterIdNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].voterIDNumber\",\n" +
                "              \"voterIdIssueDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].voterIDIssueDate\",\n" +
                "              \"voterIdExpirationDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].voterIDExpirationDate\",\n" +
                "              \"driverLicenseNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].driverLicenseNumber\",\n" +
                "              \"driverLicenseIssueDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].driverLicenseIssueDate\",\n" +
                "              \"driverLicenseExpirationDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].driverLicenseExpirationDate\",\n" +
                "              \"rationCardNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].rationCardNumber\",\n" +
                "              \"rationCardIssueDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].rationCardIssueDate\",\n" +
                "              \"rationCardExpirationDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].rationCardExpirationDate\",\n" +
                "              \"universalIdNumber\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].universalIDNumber\",\n" +
                "              \"universalIdIssueDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].universalIDIssueDate\",\n" +
                "              \"universalIdExpirationDate\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].universalIDExpirationDate\",\n" +
                "              \"emailID\": \"india\\\\.credit\\\\.caisAccountinfo.caisAccountDetails[&3].caisHolderIdDetails[&1].eMailId\"\n" +
                "\n" +
                "            }\n" +
                "          }\n" +
                "\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}]";

        System.out.println(transform(s, rule));
    }
}
