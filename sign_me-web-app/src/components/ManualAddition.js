import React from 'react';
import Cookies from 'js-cookie';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { useStyles } from './LoginRegisterStyle';
import IconButton from '@material-ui/core/IconButton'
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import Autocomplete from '@material-ui/lab/Autocomplete';
import BaseInstance from '../http-client/BaseInstance';

export default function FormDialog(props) {
    const classes = useStyles()
    const [open, setOpen] = React.useState(false)
    const [email, setEmail] = React.useState("")
    const [firstName, setFirstName] = React.useState("")
    const [lastName, setLastName] = React.useState("")

    const handleClickOpen = () => {
        setOpen(true)
    }

    const handleClose = () => {
        setOpen(false)
    }

    const addStudent = () => {
        BaseInstance.post("addAttendance", {lectureId: props.lectureId, email:email}).then(res => {
            var students = props.attendingStudents
            console.log(props.attendingStudents)
            students.push(res.data)
            console.log(props.attendingStudents)
            props.setAttendingStudents(students)
          })
          handleClose()
    }

    return (
        <div>
            <IconButton onClick={handleClickOpen}><PersonAddIcon /></IconButton>
            <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Add student attendance</DialogTitle>
                <DialogContent>
                    <div className={classes.search}>
                        <Autocomplete
                            options={props.subscribedStudents}
                            getOptionLabel={option => option.firstName + " " + option.lastName}
                            style={{ width: 300 }}
                            onChange={(event, newValue) => {
                                console.log(newValue);
                                setEmail(newValue.email)
                                setFirstName(newValue.firstName)
                                setLastName(newValue.lastName)
                            }}
                            renderInput={params => (
                                <TextField {...params} className={classes.search} placeholder="Search" variant="outlined" size="small" fullWidth />
                            )}
                        />
                    </div>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary">Cancel</Button>
                    <Button onClick={addStudent} color="primary">Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}