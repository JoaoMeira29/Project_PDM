'use strict'
const express = require('express');
const router = express.Router();
const {checkRoleAdmin, checkRoleNormal} =
    require("../middleware/rolesAuthorization");
const {getRents, getRentById, getRentByUserId, addRent, updateRent, deleteRent} =
    require("../controllers/rentController");

router.get('/listRents', checkRoleAdmin, getRents);
router.get('/listRent/:ID', checkRoleAdmin, getRentById);
router.get('/listRentByUserId/:UserID', checkRoleNormal, getRentByUserId);

router.post('/createRent', checkRoleAdmin, addRent);

router.put('/updateRent/:ID', checkRoleNormal, updateRent);

router.delete('/deleteRent/:ID', checkRoleAdmin, deleteRent);

module.exports = {
    routes: router
}