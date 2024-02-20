'use strict'
const express = require('express');
const router = express.Router();
const { checkRoleAdmin, checkRoleNormal} =
    require("../middleware/rolesAuthorization");
const { checkIfApplicationExists} =
    require("../middleware/maxNumberApplications");
const {getApplications, getApplicationById, getApplicationByUserId,
    addApplication, updateApplication, deleteApplication} =
    require("../controllers/applicationController");

router.get('/listApplications', checkRoleAdmin, getApplications);
router.get('/listApplication/:ID', checkRoleAdmin, getApplicationById);
router.get('/listWaitingApplicationsByUserID/:UserID', checkRoleAdmin, getApplicationByUserId);

router.post('/createApplication', checkRoleNormal, checkIfApplicationExists, addApplication);

router.put('/updateApplication/:ID', checkRoleAdmin, updateApplication);

router.delete('/deleteApplication/:ID', checkRoleAdmin, deleteApplication);

module.exports = {
    routes: router
}