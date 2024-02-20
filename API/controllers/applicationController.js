'use strict'
const utils = require('../utils/enums');
const applicationService = require('../services/applicationService');
const rentService = require('../services/rentService');
const roomService = require('../services/roomService');

const getApplications = async (req, res) => {
    try {
        const applications = await applicationService.listApplications();
        res.send(applications);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getApplicationById = async (req, res)=> {
    try {
        const applicationID = req.params.ID;
        const oneApplication = await applicationService.listApplicationById(applicationID);
        res.send(oneApplication);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getApplicationByUserId = async (req, res)=> {
    try {
        const userID = req.params.UserID;
        const oneApplication = await applicationService.listApplicationByUserId(userID);
        res.send(oneApplication);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const addApplication = async (req, res)=> {
    try {
        const data = req.body;
        const created = await applicationService.createApplication(data);
        res.send(created);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const updateApplication = async (req, res)=> {
    try {
        const applicationID = req.params.ID;
        const data = req.body;
        const created = await applicationService.updateApplication(applicationID, data);
        res.send(created);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteApplication = async (req, res)=> {
    try {
        const applicationID = req.params.ID;
        const deleted = await applicationService.deleteApplication(applicationID);
        res.send(deleted);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    getApplications,
    getApplicationById,
    getApplicationByUserId,
    addApplication,
    updateApplication,
    deleteApplication
}