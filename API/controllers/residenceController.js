'use strict'
const residenceService = require('../services/residenceService');
const roomService = require('../services/roomService');
const deviceService = require('../services/deviceService');

const getResidences = async (req, res) => {
    try {
        const residences = await residenceService.listResidences();
        res.send(residences);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getResidenceById = async (req, res)=> {
    try {
        const residenceID = req.params.ID;
        const oneResidence = await residenceService.listResidenceById(residenceID);
        res.send(oneResidence);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getResidenceByLocation = async (req, res)=> {
    try {
        const residenceLocation = req.body.location;
        const oneResidence = await residenceService.listResidenceByLocation(residenceLocation);
        res.send(oneResidence);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const addResidence = async (req, res)=> {
    try {
        const data = req.body;
        const residenceCreated = await residenceService.createResidence(data.first);

        for (const rt of data.second){
            for (let i = 0; i < parseInt(rt.nameValuePairs.numberOfRooms); i++) {
                 const roomCreated = await roomService.createRoom({
                     ResidenceID: residenceCreated.ID,
                     RoomTypeID: parseInt(rt.nameValuePairs.ID)
                 });

                 // ID (Respectively) -> 1, 2, 3, 4
                 // Smart Lock, Smart Air conditioning, Smart Blinds, Smart Locker
                 switch (roomCreated.RoomTypeID) {
                     case 1:
                         // Simple duplex room --> Smart Lock, Smart Blinds
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 1 })
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 3 })
                         break;
                     case 2:
                         // Simple single room --> Smart Lock, Smart Blinds
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 1 })
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 3 })
                         break;
                     case 3:
                         // Premium duplex room --> Smart Lock, Smart Air conditioning, Smart Blinds
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 1 })
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 2 })
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 3 })
                         break;
                     case 4:
                         // Premium single room --> Smart Lock, Smart Air conditioning, Smart Blinds, Smart Locker
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 1 })
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 2 })
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 3 })
                         await deviceService.createDevice({ RoomID: roomCreated.ID, DeviceTypeID: 4 })
                         break;
                     default:
                         return res.status(403).json({
                             error: "Room creation was not successful."
                         });
                 }
            }
        }

        res.send(residenceCreated);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const updateResidence = async (req, res)=> {
    try {
        const residenceID = req.params.ID;
        const data = req.body;
        const created = await residenceService.updateResidence(residenceID, data);
        res.send(created);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteResidence = async (req, res)=> {
    try {
        const residenceID = req.params.ID;
        const deleted = await residenceService.deleteResidence(residenceID);
        res.send(deleted);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    getResidences,
    getResidenceById,
    getResidenceByLocation,
    addResidence,
    updateResidence,
    deleteResidence
}