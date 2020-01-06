import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import InputBase from '@material-ui/core/InputBase';
import { fade, makeStyles } from '@material-ui/core/styles';
import MenuIcon from '@material-ui/icons/Menu';
import SearchIcon from '@material-ui/icons/Search';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Button from '@material-ui/core/Button';
import BaseInstance from '../http-client/BaseInstance'
import Cookies from 'js-cookie'

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
    display: 'none',
    [theme.breakpoints.up('sm')]: {
      display: 'block',
    },
  },
  search: {
    borderRadius: theme.shape.borderRadius,
    backgroundColor: fade(theme.palette.common.white, 0.15),
    '&:hover': {
      backgroundColor: fade(theme.palette.common.white, 0.25),
    },
  },
  searchIcon: {
    width: theme.spacing(7),
    height: '100%',
    position: 'absolute',
    pointerEvents: 'none',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  inputRoot: {
    color: 'inherit',
  },
  inputInput: {
    padding: theme.spacing(1, 1, 1, 7),
    transition: theme.transitions.create('width'),
    width: '100%',
    [theme.breakpoints.up('sm')]: {
      width: 120,
      '&:focus': {
        width: 200,
      },
    },
  },
}));


export default function SearchAppBar() {
  const classes = useStyles();
  const [open, setOpen] = React.useState(false);
  const [subject, setSubject] = React.useState("");
  const [id_course, setCourseId] = React.useState(-1);

  const handleClickOpen = () => {
    setOpen(true);
  };
  
  const handleClose = () => {
    setSubject("");
    setCourseId(-1);
    setOpen(false);
  };

  const registerToCourse = () => {
    console.log("REGISTER: " + id_course + " --- " + Cookies.get("email"))

    BaseInstance.post("createSubscriptionRequest", { 
      email: Cookies.get("email"),
      course: id_course})
      .then((res) => {
        console.log(res);
    })
    handleClose()
  }

  return (
    <div className={classes.root}>
      <AppBar position="static" style={{ "backgroundColor": "#009569" }}>
        <Toolbar>
          <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="open drawer">
            <MenuIcon />
          </IconButton>
          <Typography className={classes.title} variant="h6" noWrap>Sign Me</Typography>
          <div className={classes.search}>
            <Autocomplete
              options={JSON.parse(Cookies.get("allCourses"))}
              getOptionLabel={option => option.subject}
              style={{ width: 300 }}
              onChange={(event, newValue) => {
                console.log(newValue);
                setSubject(newValue.subject)
                setCourseId(newValue.courseId)
                handleClickOpen()
              }}
              renderInput={params => (
                <TextField {...params} className={classes.search} placeholder="Search" variant="outlined" size="small" fullWidth />
              )}
            />
          </div>
        </Toolbar>
      </AppBar>



       <Dialog open={open} onClose={handleClose} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
        <DialogTitle id="alert-dialog-title">Do you want to send your invitation request to "{subject}" course?</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            The professor will handle your subscription request as soon as possible.  
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Disagree
          </Button>
          <Button onClick={registerToCourse} color="primary" autoFocus>
            Agree
          </Button>
        </DialogActions>
      </Dialog> 


    </div>
  );
}