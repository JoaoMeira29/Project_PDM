'use strict'
const userService = require('../services/userService');
const { join} = require("path");

const getUsers = async (req, res) => {
    try {
        const users = await userService.listUsers();
        res.send(users);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getUserById = async (req, res)=> {
    try {
        const userID = req.params.ID;
        const oneUser = await userService.listUserById(userID);
        res.send(oneUser);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getUserByEmail = async (req, res)=> {
    try {
        const userEmail = req.body.email;
        const oneUser = await userService.listUserByEmail(userEmail);
        res.send(oneUser);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const getUserProfilePhoto = async (req, res)=> {
    try {
        // TODO IMPLEMENT THE UPLOAD USER PHOTO
        const userID = req.params.ID;
        const user = await userService.listUserById(userID);
        if (!user)
            return res.status(404).json({ message: 'User not found' });
        else if (user.userPhoto == null)
            return res.status(200).json({ message: 'User doesn\'t have profile photo' });

        res.sendFile(join(__dirname, user.userPhoto));
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const addUser = async (req, res)=> {
    try {
        const data = req.body;
        const created = await userService.createUser(data);
        res.send(created);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const updateUser = async (req, res)=> {
    try {
        const userID = req.params.ID;
        const data = req.body;
        const created = await userService.updateUser(userID, data);
        res.send(created);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const updateUserStatus = async (req, res)=> {
    try {
        const userID = req.params.ID;
        const data = req.body;
        const created = await userService.updateUserStatus(userID, data);
        res.send(created);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteUser = async (req, res)=> {
    try {
        const userID = req.params.ID;
        const deleted = await userService.deleteUser(userID);
        res.send(deleted);
    }
    catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    getUsers,
    getUserById,
    getUserByEmail,
    getUserProfilePhoto,
    addUser,
    updateUser,
    updateUserStatus,
    deleteUser
}