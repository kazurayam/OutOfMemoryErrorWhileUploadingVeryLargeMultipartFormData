import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS


def response = WS.sendRequest(findTestObject('POST multipart form-data very large files'))
