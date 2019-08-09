package com.pratham.pradigikids.util;

public class PD_Error {

    public static String ERROR_5000 = "Connection time out. Please try again.";
    public static String ERROR_5001 = "Socket time out. Please try again.";
    public static String ERROR_5002 = "Internet connectivity missing, check your network settings.";
    public static String ERROR_4000 = "Something went wrong, try again.";

    public static String ERROR_100 = "Continue";
    public static String ERROR_101 = "Switching Protocols";
    public static String ERROR_200 = "OK";
    public static String ERROR_201 = "Created";
    public static String ERROR_202 = "Accepted";
    public static String ERROR_203 = "Non-Authoritative Information";
    public static String ERROR_204 = "No Content";
    public static String ERROR_205 = "Reset Content";
    public static String ERROR_206 = "Partial Content";
    public static String ERROR_300 = "Multiple Choices";
    public static String ERROR_301 = "Moved Permanently";
    public static String ERROR_302 = "Found";
    public static String ERROR_303 = "See Other";
    public static String ERROR_304 = "Not Modified";
    public static String ERROR_305 = "Use Proxy";
    public static String ERROR_306 = "(Unused)";
    public static String ERROR_307 = "Temporary Redirect";
    public static String ERROR_400 = "Bad Request";
    public static String ERROR_401 = "The email or password you entered is incorrect. Please check and try again.";
    public static String ERROR_402 = "Payment Required";
    public static String ERROR_403 = "Forbidden";
    public static String ERROR_404 = "Not Found";
    public static String ERROR_405 = "Method Not Allowed";
    public static String ERROR_406 = "Not Acceptable";
    public static String ERROR_407 = "Proxy Authentication Required";
    public static String ERROR_408 = "Request Timeout";
    public static String ERROR_409 = "Conflict";
    public static String ERROR_410 = "Gone";
    public static String ERROR_411 = "Length Required";
    public static String ERROR_412 = "Precondition Failed";
    public static String ERROR_413 = "Request Entity Too Large";
    public static String ERROR_414 = "Request-URI Too Long";
    public static String ERROR_415 = "Unsupported Media Type";
    public static String ERROR_416 = "Requested Range Not Satisfiable";
    public static String ERROR_417 = "Expectation Failed";
    public static String ERROR_500 = "Internal Server Error";
    public static String ERROR_501 = "Not Implemented";
    public static String ERROR_502 = "Bad Gateway";
    public static String ERROR_503 = "Service Unavailable";
    public static String ERROR_504 = "Gateway Timeout";
    public static String ERROR_505 = "HTTP Version Not Supported";
    public static String ERROR_506 = "Categories has been successfully fetched";
    public static String ERROR_507 = "Categories not found";
    public static String ERROR_508 = "The Email you provided is already registered with account. Please try using another email address.";

    // The code you've entered is incorrect, please enter valid code.

    // An email has been sent to emailaddress along with a reset code. You
    // can use that code to reset your password.

    // The email you entered is not associated with any account. Please enter a
    // valid email address and try again.

    // 100 => 'Continue',
    // 101 => 'Switching Protocols',
    // 200 => 'OK',
    // 201 => 'Created',
    // 202 => 'Accepted',
    // 203 => 'Non-Authoritative Information',
    // 204 => 'No Content',
    // 205 => 'Reset Content',
    // 206 => 'Partial Content',
    // 300 => 'Multiple Choices',
    // 301 => 'Moved Permanently',
    // 302 => 'Found',
    // 303 => 'See Other',
    // 304 => 'Not Modified',
    // 305 => 'Use Proxy',
    // 306 => '(Unused)',
    // 307 => 'Temporary Redirect',
    // 400 => 'Bad Request',
    // 401 => 'Unauthorized',
    // 402 => 'Payment Required',
    // 403 => 'Forbidden',
    // 404 => 'Not Found',
    // 405 => 'Method Not Allowed',
    // 406 => 'Not Acceptable',
    // 407 => 'Proxy Authentication Required',
    // 408 => 'Request Timeout',
    // 409 => 'Conflict',
    // 410 => 'Gone',
    // 411 => 'Length Required',
    // 412 => 'Precondition Failed',
    // 413 => 'Request Entity Too Large',
    // 414 => 'Request-URI Too Long',
    // 415 => 'Unsupported Media Type',
    // 416 => 'Requested Range Not Satisfiable',
    // 417 => 'Expectation Failed',
    // 500 => 'Internal Server Error',
    // 501 => 'Not Implemented',
    // 502 => 'Bad Gateway',
    // 503 => 'Service Unavailable',
    // 504 => 'Gateway Timeout',
    // 505 => 'HTTP Version Not Supported',
    // 506 => 'Categories has been successfully fetched',
    // 507 => 'Categories not found'

    public static String GetError(int ErrorCode) {
        String str_ErrorMessage = "";

        switch (ErrorCode) {
            case 5000:
                str_ErrorMessage = ERROR_5000;
                break;

            case 5001:
                str_ErrorMessage = ERROR_5001;
                break;

            case 5002:
                str_ErrorMessage = ERROR_5002;
                break;

            case 4000:
                str_ErrorMessage = ERROR_4000;
                break;

            case 100:
                str_ErrorMessage = ERROR_100;
                break;
            case 101:
                str_ErrorMessage = ERROR_101;
                break;
            case 200:
                str_ErrorMessage = ERROR_200;
                break;
            case 201:
                str_ErrorMessage = ERROR_201;
                break;
            case 202:
                str_ErrorMessage = ERROR_202;
                break;
            case 203:
                str_ErrorMessage = ERROR_203;
                break;
            case 204:
                str_ErrorMessage = ERROR_204;
                break;
            case 205:
                str_ErrorMessage = ERROR_205;
                break;
            case 206:
                str_ErrorMessage = ERROR_206;
                break;
            case 300:
                str_ErrorMessage = ERROR_300;
                break;
            case 301:
                str_ErrorMessage = ERROR_301;
                break;
            case 302:
                str_ErrorMessage = ERROR_302;
                break;
            case 303:
                str_ErrorMessage = ERROR_303;
                break;
            case 304:
                str_ErrorMessage = ERROR_304;
                break;
            case 305:
                str_ErrorMessage = ERROR_305;
                break;
            case 306:
                str_ErrorMessage = ERROR_306;
                break;
            case 400:
                str_ErrorMessage = ERROR_400;
                break;
            case 401:
                str_ErrorMessage = ERROR_401;
                break;
            case 402:
                str_ErrorMessage = ERROR_402;
                break;
            case 403:
                str_ErrorMessage = ERROR_403;
                break;
            case 404:
                str_ErrorMessage = ERROR_404;
                break;
            case 405:
                str_ErrorMessage = ERROR_405;
                break;
            case 406:
                str_ErrorMessage = ERROR_406;
                break;
            case 407:
                str_ErrorMessage = ERROR_407;
                break;
            case 408:
                str_ErrorMessage = ERROR_408;
                break;
            case 409:
                str_ErrorMessage = ERROR_409;
                break;
            case 410:
                str_ErrorMessage = ERROR_410;
                break;
            case 411:
                str_ErrorMessage = ERROR_411;
                break;
            case 412:
                str_ErrorMessage = ERROR_412;
                break;
            case 413:
                str_ErrorMessage = ERROR_413;
                break;

            case 414:
                str_ErrorMessage = ERROR_414;
                break;

            case 415:
                str_ErrorMessage = ERROR_415;
                break;

            case 416:
                str_ErrorMessage = ERROR_416;
                break;

            case 417:
                str_ErrorMessage = ERROR_417;
                break;

            case 500:
                str_ErrorMessage = ERROR_500;
                break;
            case 501:
                str_ErrorMessage = ERROR_501;
                break;
            case 502:
                str_ErrorMessage = ERROR_502;
                break;
            case 503:
                str_ErrorMessage = ERROR_503;
                break;
            case 504:
                str_ErrorMessage = ERROR_504;
                break;

            case 505:
                str_ErrorMessage = ERROR_505;
                break;
            case 506:
                str_ErrorMessage = ERROR_506;
                break;

            case 507:
                str_ErrorMessage = ERROR_507;
                break;
            case 508:
                str_ErrorMessage = ERROR_508;
                break;

            default:
                break;
        }

        return str_ErrorMessage;

    }

}
