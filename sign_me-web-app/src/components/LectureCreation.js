import React from 'react';
import BaseInstance from '../http-client/BaseInstance'
import Button from '@material-ui/core/Button';
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Grid from '@material-ui/core/Grid';
import {useStyles} from './LoginRegisterStyle'
import 'date-fns';
import DateFnsUtils from '@date-io/date-fns';
import {
  MuiPickersUtilsProvider,
  KeyboardDatePicker,
} from '@material-ui/pickers';

  

export default function FormDialog(props) {
  const classes = useStyles()
  const [open, setOpen] = React.useState(false)
  const [selectedDate, setSelectedDate] = React.useState(new Date('2014-08-18T21:11:54'))
  const [description, setSelectedDescription] = React.useState("")
  
  const handleClickOpen = () => {
    setOpen(true)
  }

  const handleClose = () => {
    setOpen(false)
  }

  const handleDateChange = date => {
    setSelectedDate(date)
  }

  const handleDescriptionChange = (e) => {
      setSelectedDescription(e.target.value)
  }

  const createLecture = () => {
    console.log(description)
    console.log(selectedDate.toDateString())
    /* BaseInstance etc.. */
    handleClose()
  }

  return (
    <div style={{ display:"inline-block", float:"right", paddingRight:40}}>
      <Fab className={classes.buttonStyle} color="primary" onClick={handleClickOpen} variant="extended">
      Create Lecture
      </Fab>
      <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
        <DialogTitle id="form-dialog-title">New Lecture</DialogTitle>
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
              />
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
        </form>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={createLecture} color="primary">
            Create
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}