'use strict';
const utils = require('../utils/enums');
const Room = require('../models/roomModel');
const Residence = require('../models/residenceModel');
const RoomType = require('../models/roomTypeModel');

Room.belongsTo(Residence, { foreignKey: 'ResidenceID' });
Room.belongsTo(RoomType, { foreignKey: 'RoomTypeID' });

const listRooms = async () => {
    try {
        return await Room.findAll({
            include: [{
                model: Residence,
                attributes: ['name'],
                required: true,
            },{
                model: RoomType,
                attributes: ['name'],
                required: true,
            }]
        });
    } catch (error) {
        return error.message;
    }
}

/**
 * Retrieves a list of unoccupied rooms by their RoomTypeID and ResidenceID.
 * @async
 * @param {number} RoomTypeID - ID of the roomType to search for.
 * @param {number} ResidenceID - ID of the residence to search for.
 * @returns {Promise<Array<Room> | string>} - A promise that resolves with an array of room objects
 * or an error message.
 */
const listRoomsUnoccupied = async (RoomTypeID, ResidenceID) => {
    try {
        return await Room.findAll({
            include: [{
                model: Residence,
                attributes: ['name'],
                required: true,
            },{
                model: RoomType,
                attributes: ['name'],
                required: true,
            }],
            where: {
                Status: "Unoccupied",
                RoomTypeID: RoomTypeID,
                ResidenceID: ResidenceID
            }
        });
    } catch (error) {
        return error.message;
    }
}

const listRoomById = async (Id)=> {
    try {
        return await Room.findByPk(Id, {
            include: [{
                model: Residence,
                attributes: ['name'],
                required: true,
            },{
                model: RoomType,
                attributes: ['name'],
                required: true,
            }]
        });
    }
    catch (error) {
        return  error.message;
    }
}

const createRoom = async (roomData) => {
    try {
        const newRoom = await Room.create({
            status: utils.roomStatus.Unoccupied,
            ResidenceID: roomData.ResidenceID,
            RoomTypeID: roomData.RoomTypeID
        });

        return newRoom.toJSON();
    }
    catch (error) {
        return  error.message;
    }
}

const updateRoom = async (ID, roomData) => {
    try {
        const [updatedCount] = await Room.update(roomData, {
            where: { ID: ID },
        });

        if (updatedCount === 0)
            return { message: 'Something went wrong! Record not updated' };

        const updatedRoom = await Room.findByPk(ID);
        return updatedRoom.toJSON();
    }
    catch (error) {
        return error.message;
    }
}

const updateRoomStatus = async (ID, newStatus) => {
    try {
        const [updatedCount] = await Room.update({
            status: newStatus
        },{
            where: {ID: ID},
        });

        if (updatedCount === 0)
            return { message: 'Something went wrong! Record not updated' };

        const updatedRoom = await Room.findByPk(ID);
        return updatedRoom.toJSON();
    }
    catch (error) {
        return error.message;
    }
}

const deleteRoom = async (ID) => {
    try {
        const deletedCount = await Room.destroy({
            where: { ID: ID },
        });

        if (deletedCount === 0)
            return { message: 'Something went wrong! Record not deleted' };

        return { message: 'Record deleted successfully' };
    }
    catch (error) {
        return error.message;
    }
}

module.exports = {
    listRooms,
    listRoomsUnoccupied,
    listRoomById,
    createRoom,
    updateRoom,
    updateRoomStatus,
    deleteRoom
}