import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { fade, makeStyles } from '@material-ui/core/styles';
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
import HomeOutlinedIcon from '@material-ui/icons/HomeOutlined';
import ExitToAppOutlinedIcon from '@material-ui/icons/ExitToAppOutlined';
import {createBrowserHistory} from 'history'
import Home from '@material-ui/icons/Home';
import Tooltip from '@material-ui/core/Tooltip'


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
    backgroundColor: 'white',
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
  logoutIcon: {
    'margin-left': '20px'
  }
}));


export default function SearchAppBar({history}) {
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
    if(Cookies.get("profilePicture") === "null"){
      alert("Please set a profile picture to subscribe for courses")
      handleClose()
      return
    }
    BaseInstance.post("createSubscriptionRequest", {
      email: Cookies.get("email"),
      course: id_course
    })
      .then((res) => {
        console.log(res);
      })
    handleClose()
  }

  const redirectHome = () => {
    if (history === undefined) {
      history = createBrowserHistory()
    }
    history.push("/home")
  }

  const redirectLogout = () => {
    if (history === undefined) {
      history = createBrowserHistory()
    }
    history.push("/logout")
  }
  return (
    <div className={classes.root}>
      <AppBar position="static" style={{ "backgroundColor": "#009569" }}>
        <Toolbar>
          <Tooltip title="Back home">
            <IconButton edge="start" onClick={() => redirectHome()} className={classes.menuButton} color="inherit" aria-label="open drawer">
              <Home/>
            </IconButton>
          </Tooltip>
          <Typography className={classes.title} variant="h6" noWrap><b>Sign Me</b></Typography>
          <div className={classes.search}>
            <Autocomplete
              disableClearable
              disableOpenOnFocus
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
          <Tooltip title="Logout">
            <IconButton edge="start" onClick={() => redirectLogout()} className={classes.logoutIcon} color="inherit" aria-label="open drawer">
              <ExitToAppOutlinedIcon />
            </IconButton>
          </Tooltip>
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