'use strict'
const express = require('express');
const router = express.Router();
const {checkRoleAdmin, checkRoleNormal} =
    require("../middleware/rolesAuthorization");
const {getPayments, getPaymentsByUserId, getPaymentById,
    addPayment, updatePayment, updatePaymentStatus, deletePayment} =
    require("../controllers/paymentController");

router.get('/listPayments', checkRoleAdmin, getPayments);
router.get('/listPaymentsByUserID/:UserID', checkRoleNormal, getPaymentsByUserId);
router.get('/listPayment/:ID', checkRoleNormal, getPaymentById);

router.post('/createPayment', checkRoleAdmin, addPayment);

router.put('/updatePayment/:ID', checkRoleNormal, updatePayment);
router.put('/updatePaymentStatus/:ID', checkRoleNormal, updatePaymentStatus);

router.delete('/deletePayment/:ID', checkRoleAdmin, deletePayment);

module.exports = {
    routes: router
}