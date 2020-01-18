import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import SpeedDial from '@material-ui/lab/SpeedDial';
import SpeedDialIcon from '@material-ui/lab/SpeedDialIcon';
import SpeedDialAction from '@material-ui/lab/SpeedDialAction';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Grid from '@material-ui/core/Grid';
import BaseInstance from '../http-client/BaseInstance'
import 'date-fns';
import DateFnsUtils from '@date-io/date-fns';
import {
    MuiPickersUtilsProvider,
    KeyboardDatePicker,
} from '@material-ui/pickers';

import TextField from '@material-ui/core/TextField';
import { Button } from '@material-ui/core';
import Cookies from 'js-cookie'
import Fab from '@material-ui/core/Fab';
import AttachFileIcon from '@material-ui/icons/AttachFile';
import NotificationsIcon from '@material-ui/icons/Notifications';
import MenuBookIcon from '@material-ui/icons/MenuBook';

const useStyles = makeStyles(theme => ({
    root: {
        transform: 'translateZ(0px)',
        flexGrow: 1,
    },
    exampleWrapper: {
        position: 'relative',
        marginTop: theme.spacing(3),
        height: 380,
    },
    radioGroup: {
        margin: theme.spacing(1, 0),
    },
    speedDial: {
        position: 'absolute',
        '&.MuiSpeedDial-directionUp, &.MuiSpeedDial-directionLeft': {
            bottom: theme.spacing(2),
            right: theme.spacing(2),
        },
        '&.MuiSpeedDial-directionDown, &.MuiSpeedDial-directionRight': {
            top: theme.spacing(2),
            left: theme.spacing(2),
        },
    }, lectureCreation: {
        background: 'linear-gradient(45deg, #009569 30%, #008B5D 90%)',
        "position": "absolute",
        "right": "3%",
        "top": "150px",
        "font-size": "80% !important",
    }
}));

const actions = [
    { icon: <MenuBookIcon/>, name: "Create Lecture" },
    { icon: <AttachFileIcon/> , name: 'Create Material' },
    { icon: <NotificationsIcon/>, name: 'Create Notice' },
];

export default function SpeedDials(props) {
    const [selectedDate, setSelectedDate] = React.useState(new Date('2020-01-09T21:11:54'))
    const [description, setSelectedDescription] = React.useState("")
    const classes = useStyles();
    const [open, setOpen] = React.useState(false);
    const [dialogOpen, setDialogOpen] = React.useState(false);
    const [actionPicked, setActionPicked] = React.useState("");
    const [formData, setFormData] = React.useState("");
    const [disabledDescription, setDisabledDescription] = React.useState(false)

    const handleDateChange = date => {
        setSelectedDate(date)
    }

    const handleDialogClose = () => {
        setFormData("")
        setDisabledDescription(false)
        setSelectedDescription("")
        setDialogOpen(false)
    }

    const handleDescriptionChange = (e) => {
        setSelectedDescription(e.target.value)
    }

    const handleCreateAction = () => {
        if(actionPicked === "Create Lecture") {
            createLecture()
        } else if(actionPicked === "Create Material") {
            uploadMaterial()
        } else if(actionPicked === "Create Notice") {
            createNotice()
        }
    }

    const uploadMaterial = () => {
        BaseInstance.post("uploadMaterial", formData)
            .then(res => {
                console.log(res.data);
                var path = res.data.materialPath
                var tmp = JSON.parse(Cookies.get("material"))
                tmp.push( {"materialId": res.data.materialId, "materialPath": path, "description": description} )
                console.log(props)
                tmp.sort((a,b) => (a.materialId > b.materialId)? 1 : -1)
                props.setMaterial(tmp)
                Cookies.set("material", tmp)

                handleDialogClose()
        })
    }

    const createNotice = () => {
        BaseInstance.post("createNotice", {
            "noticeDescription" : description,
            "courseId": JSON.parse(Cookies.get("currentCourse")).courseId
        }).then((res) => {
            console.log(res.data)
            var currentNotices = JSON.parse(Cookies.get("courseNotices"))
            currentNotices.push(res.data)
            currentNotices.sort((a,b) => (a.noticeId > b.noticeId)? 1 : -1)
            props.setNotices(currentNotices)
            Cookies.set("courseNotices", currentNotices)
        })
        handleDialogClose()
    }

    const createLecture = () => {
        var courseId = JSON.parse(Cookies.get("currentCourse")).courseId;
        var lectures = []
        BaseInstance.post("createLecture", { course: courseId, description: description, date: selectedDate }).then(res => {
            lectures = JSON.parse(Cookies.get("currentLectures"));
            lectures.push(res.data);
            lectures.sort((a, b) => (a.date > b.date) ? 1 : -1)
            props.setLectures(lectures)
            Cookies.set("currentLectures", lectures);
        })
        handleDialogClose()
    }

    const handleClick = (actionName) => {
        handleClose();
        console.log(actionName)
        setActionPicked(actionName)
        setDialogOpen(true);

    }

    const handleClose = () => {
        setOpen(false);
    };

    const handleOpen = () => {
        setOpen(true);
    };

    const onFileChangeHandler = (e) => {
        e.preventDefault();
        console.log("update")
        const formData = new FormData();
        var nameFile = e.target.files[0].name;
        formData.append('file', e.target.files[0], JSON.parse(Cookies.get("currentCourse")).courseId + "_" + description + "_" + nameFile);
        setFormData(formData)
        setDisabledDescription(true)
    };


    return (
        <div className={classes.lectureCreation} style={{ display: "inline-block", float: "right" }}>
            <SpeedDial
                ariaLabel="SpeedDial example"
                className={classes.speedDial}
                hidden={false}
                icon={<SpeedDialIcon />}
                onClose={handleClose}
                onOpen={handleOpen}
                open={open}
                direction={"left"}
            >
                {actions.map(action => (
                    <SpeedDialAction
                        key={action.name}
                        icon={action.icon}
                        tooltipTitle={action.name}
                        onClick={() => handleClick(action.name)}
                    />
                ))}
            </SpeedDial>
            {<Dialog open={dialogOpen} onClose={handleDialogClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">{actionPicked}</DialogTitle>
                <DialogContent>
                    <form>
                        <TextField
                            autoFocus
                            margin="dense"
                            id="description"
                            label="Description"
                            onChange={handleDescriptionChange}
                            required
                            fullWidth
                            disabled={disabledDescription}
                        />
                        {
                            actionPicked === "Create Lecture" ? (
                                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                    <Grid container justify="space-around">
                                        <KeyboardDatePicker
                                            margin="normal"
                                            id="date-picker-dialog"
                                            label="Day of the lecture"
                                            format="dd/MM/yyyy"
                                            value={selectedDate}
                                            onChange={handleDateChange}
                                            KeyboardButtonProps={{
                                                'aria-label': 'change date',
                                            }}
                                            required
                                        />
                                    </Grid>
                                </MuiPickersUtilsProvider>
                            ) : null
                        }
                        {
                            actionPicked === "Create Material" ? (
                                <Fab component="label" color="primary" variant="extended" disabled={description===""}>
                                    Update
                                    <input type="file" style={{ display: "none" }} onChange={onFileChangeHandler} accept=".pdf,.ppt,.pptx,.zip" />
                                </Fab>
                                
                            ) : null
                        }

                    </form>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleCreateAction} color="primary" >
                        Create
                    </Button>
                </DialogActions>
            </Dialog>}
        </div>
    );
}
