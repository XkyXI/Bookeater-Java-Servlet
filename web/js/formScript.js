function isEmpty (str) {
    return str == null || str === "" || str.replace(/\s+/, '').length === 0;
}

function checkPattern (str, pat) {
    return RegExp(pat).test(str);
}

// validate the form input
function validateForm() {
    var f = document.forms["orderForm"];
    var fname = f["firstname"].value;
    var lname = f["lastname"].value;
    var pnum = f["phone"].value;
    var addr = f["address"].value;
    var city = f["city"].value;
    var state = f["state"].value;
    var shipping = f["shipping"].value;
    var cardname = f["cardname"].value;
    var cnum = f["cardnumber"].value;
    var exprdate = f["exprdate"].value;
    var ccode = f["cvv"].value;
    var pcode = f["zipcode"].value;

    // check everything is filled in
    if (isEmpty(fname) || isEmpty(lname) || isEmpty(pnum) || isEmpty(addr) ||
        isEmpty(shipping) || isEmpty(cardname) || isEmpty(cnum) || isEmpty(exprdate) ||
        isEmpty(ccode) || isEmpty(pcode) || isEmpty(city) || isEmpty(state)) {
        alert("All entries must be filled out");
        return false;
    }

    // the following validate all the format
    if (!checkPattern(pnum, "^[0-9]{3}-[0-9]{3}-[0-9]{4}$")) {
        alert("Invalid phone number format");
        return false;
    }
    // need to double escape because of escaping -
    if (!checkPattern(addr, "^[A-Za-z0-9\\.\\,\\'\\-\\s\\#]+$")) {
        alert("Address contains invalid characters");
        return false;
    }
    if (!checkPattern(city, "^[A-Za-z\\s]+$")) {
        alert("City contains invalid characters");
        return false;
    }
    if (!checkPattern(state, "^[A-Za-z]+$")) {
        alert("State contains invalid characters");
        return false;
    }
    if (!checkPattern(cnum, "^[0-9]+$")) {
        alert("Card number should only contain number");
        return false;
    }
    if (!checkPattern(exprdate, "^[0-9][0-9]\\/[0-9][0-9]$")) {
        alert("Invalid expiration date format");
        return false;
    }
    if (!checkPattern(ccode, "^[0-9]{3}$")) {
        alert("Invalid credit card security code format");
        return false;
    }
    if (!checkPattern(pcode, "^[0-9]{5}(-[0-9]{4})?$")) {
        alert("Invalid ZIP/postal code format");
        return false;
    }

    return true;
}
