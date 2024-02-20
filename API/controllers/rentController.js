'use strict'
const userService = require('../services/userService');
const applicationService = require('../services/applicationService');
const rentService = require('../services/rentService');
const roomService = require('../services/roomService');
const chatService = require('../services/chatService');
const participantService = require('../services/participantService');
const utils = require("../utils/enums");

const getRents = async (req, res) => {
    try {
        const rents = await rentService.listRents();
        res.send(rents);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getRentById = async (req, res)=> {
    try {
        const rentID = req.params.ID;
        const oneRent = await rentService.listRentById(rentID);
        res.send(oneRent);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getRentByUserId = async (req, res)=> {
    try {
        const userID = req.params.UserID;
        const oneRent = await rentService.listRentByUserId(userID);
        res.send(oneRent);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const addRent = async (req, res)=> {
    try {
        const data = req.body.nameValuePairs;

        const user = await userService.listUserById(data.UserID)
        if (user.status === utils.userStatus.Inactive) {
            const roomsUnoccupied = await roomService.listRoomsUnoccupied(data.RoomTypeID, data.ResidenceID);
            const userApplication = await applicationService.listApplicationByUserId(data.UserID);

            if (Object.keys(roomsUnoccupied).length === 0) {
                await applicationService.updateApplication(userApplication.ID, utils.applicationStatus.AcceptedButWaiting);
                return res.status(403).json({
                    error: "No room available from that type."
                });
            }
            else {
                const created = await rentService.createRent({
                    UserID: data.UserID,
                    RoomID: roomsUnoccupied[0].ID
                });
                await roomService.updateRoomStatus(roomsUnoccupied[0].ID, utils.roomStatus.Occupied);
                await applicationService.updateApplication(userApplication.ID, { status: utils.applicationStatus.Accepted });
                await userService.updateUser(data.UserID, { status: utils.userStatus.Active })

                const chatAdmin = await chatService.createChat({ChatTypeID: 3});
                await participantService.createParticipant({UserID: data.UserID, ChatID: chatAdmin.ID})
                //FIXME CHANGE THIS PARTICIPANTS CONNECTIONG TO WORK WITH THE RESIDENCE ADMINS LIST
                await participantService.createParticipant({UserID: 1, ChatID: chatAdmin.ID})
                await participantService.createParticipant({UserID: 2, ChatID: chatAdmin.ID})

                res.send(created);
            }
        }
        else {
            return res.status(403).json({
                error: "User already has a rent."
            });
        }
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const updateRent = async (req, res)=> {
    try {
        const rentID = req.params.ID;
        const data = req.body;
        const created = await rentService.updateRent(rentID, data);
        res.send(created);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteRent = async (req, res)=> {
    try {
        const rentID = req.params.ID;
        const deleted = await rentService.deleteRent(rentID);
        res.send(deleted);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    getRents,
    getRentById,
    getRentByUserId,
    addRent,
    updateRent,
    deleteRent
}