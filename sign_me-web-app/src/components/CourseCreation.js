import React from 'react';
import BaseInstance from '../http-client/BaseInstance'
import Button from '@material-ui/core/Button';
import Fab from '@material-ui/core/Fab';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import {useStyles} from './LoginRegisterStyle'
import Cookies from 'js-cookie'

  

export default function FormDialog(props) {
  const classes = useStyles()
  const [open, setOpen] = React.useState(false)
  const [description, setSelectedDescription] = React.useState("")
  
  const handleClickOpen = () => {
    setOpen(true)
  }

  const handleClose = () => {
    setOpen(false)
  }

  const handleDescriptionChange = (e) => {
      setSelectedDescription(e.target.value)
  }

  const addCourse = (id, name) => {
    var courses = JSON.parse(Cookies.get("createdCourses"))
    console.log(courses)
    courses.push({ "courseId": id, "subject": name })
    Cookies.set("createdCourses", courses)
    props.setCourses(courses)
  }

  const createCourse = () => {
    console.log(description)
    BaseInstance.post("createCourse", { email: Cookies.get("email"), subject: description }).then(res => {
      addCourse(res.data, description)
    })
    handleClose()
  }

  return (
    <div style={{ display:"inline-block", float:"right", paddingRight:10}}>
      <Fab style={{ "margin_left":"10px", "width":"100% !important"}} className={classes.buttonStyle} color="primary" onClick={handleClickOpen} variant="extended">
      Create Course
      </Fab>
      <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
        <DialogTitle id="form-dialog-title">Create new course</DialogTitle>
        <DialogContent>
        <form>
              <TextField
                autoFocus
                margin="dense"
                id="description"
                label="Course name"
                onChange={handleDescriptionChange}
                required
                fullWidth/>
        </form>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={createCourse} color="primary">
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}