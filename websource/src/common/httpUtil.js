import axios from "axios";

const dateReg = /\d{4}(\-|\/|\.)\d{1,2}\1\d{1,2}/;

const protocol = window.top.location.protocol;
const hostname = window.top.location.hostname;
const port = window.top.location.port;

const prodPrefix = protocol + "//" + hostname + ":" + port;
const devPrefix = protocol + "//" + hostname + ":9999";
const pre = process.env.NODE_ENV === 'production' ? prodPrefix : devPrefix;


const msgRex = /"message":"([\S|\s]*)",/;

export function ajaxGet(url, success, fail) {
    axios.get(getFinalRequestUrl(url), {withCredentials: true})
        .then(function (response) {
            let result = response.data;
            forEachReplaceDateByKey(result);
            success(result);
        })
        .catch(function (error) {
            console.log(error);
            console.log(error.config);
            let msg = '';
            if (error.response) {
                msg = error.response.data;
            } else if (error.request) {
                msg = JSON.stringify(error.request);
            } else {
                msg = error.message;
            }
            if (fail) {
                fail(msg);
            }
        });
};

export function ajaxPost(url, data, success, fail) {
    axios.post(getFinalRequestUrl(url), data || {}, {withCredentials: true})
        .then(function (response) {
            success(response.data);
        })
        .catch(function (error) {
            console.log(error);
            console.log(error.config);
            let msg = '';
            if (error.response) {
                msg = error.response.data;
            } else if (error.request) {
                msg = JSON.stringify(error.request);
            } else {
                msg = error.message;
            }
            if (fail) {
                fail(msg);
            }
        });
};


function getDate(str) {
    try {
        return new Date(Date.parse(new Date(str)));
    } catch (e) {
    }
    return null;
}

function forEachReplaceDateByKey(obj) {
    if (obj instanceof Array) {
        for (var i in obj) {
            forEachReplaceDateByKey(obj[i]);
        }
    } else if (obj instanceof Object) {
        for (var k in obj) {
            if (dateReg.test(obj [k])) {
                obj [k] = getDate(obj [k]) || obj[k];
            }
        }
    }
}


function getFinalRequestUrl(url) {
    console.info('process.env.NODE_ENV', process.env.NODE_ENV, "url", url);
    if (url == null || url.length == 0) {
        throw "url is null or empty!";
    }
    if (url.indexOf('http') == 0) {
        return url;
    }
    if (url.indexOf('/') != 0) {
        url = '/' + url;
    }
    return pre + url;
}
