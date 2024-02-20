'use strict'
const jwt = require('jsonwebtoken');
const userService = require('../services/userService');
const applicationService = require('../services/applicationService');
const utils = require('../utils/enums');

const checkIfApplicationExists = async (req, res, next) => {
    let token;
    let authHeader = req.headers['authorization'];

    if (!authHeader)
        return res.status(401).json({ message: 'Token not provided' });

    if (authHeader.indexOf(' ') >= 0)
        token = authHeader.split(' ')[1];
    else
        token = authHeader

    try{
        const jwtDecoded = jwt.verify(token, process.env.SECRET_TOKEN);
        const foundUser = await userService.listUserById(jwtDecoded.user.ID);
        const userApplications = await applicationService.listApplicationByUserId(foundUser.ID);

        if (userApplications == null) {
            next();
        }
        else {
            return res.status(401).json({
                error: 'You can only apply once per year!'
            });
        }
    }
    catch (error){
        return res.status(401).json({ error: `${error.message}` });
    }
}


module.exports = {
    checkIfApplicationExists,
}