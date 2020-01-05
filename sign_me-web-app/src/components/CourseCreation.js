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

  const createCourse = () => {
    console.log(description)
    BaseInstance.post("createCourse", { email: props.getUser(), subject: description }).then(res => { 
      alert(res.data) 
      props.addCourse(res.data, description)
    })
    handleClose()
  }

  return (
    <div style={{ display:"inline-block", float:"right", paddingRight:40}}>
      <Fab className={classes.buttonStyle} color="primary" onClick={handleClickOpen} variant="extended">
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