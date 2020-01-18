import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { Button, ExpansionPanelActions, Grid, Card } from '@material-ui/core';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import DeleteIcon from '@material-ui/icons/Delete';
import AddAPhotoIcon from '@material-ui/icons/AddAPhoto';
import PersonAddDisabledIcon from '@material-ui/icons/PersonAddDisabled';
import IconButton from '@material-ui/core/IconButton'
import Cookies from 'js-cookie'
import BaseInstance from '../http-client/BaseInstance'
import ManualAddition from "./ManualAddition"
import LectureCreation from "./LectureCreation"
import CircularProgress from '@material-ui/core/CircularProgress';
import FabGroup from "./FabGroup"
import MenuBookIcon from '@material-ui/icons/MenuBook';

const useStyles = makeStyles(theme => ({
  root: {
    width: '100%',
  },
  heading: {
    fontSize: theme.typography.pxToRem(15),
    flexBasis: '33.33%',
    flexShrink: 0,
  },
  secondaryHeading: {
    fontSize: theme.typography.pxToRem(15),
    color: theme.palette.text.secondary,
  },
  loading: {
    display: 'flex',
    marginLeft: '50%'
  },
}));


export default function ControlledExpansionPanels(props) {
  const classes = useStyles();
  const [expanded, setExpanded] = React.useState(false)
  const [attendingStudents, setAttendingStudents] = React.useState([])
  const [lectures, setLectures] = React.useState(JSON.parse(Cookies.get("currentLectures")))
  const [uploading, setUploading] = React.useState(false)
  

  const onFileChangeHandler = (e, lectureId) => {
    e.preventDefault();
    console.log("upload")
    setUploading(true)
    const formData = new FormData();
    var nameFile = e.target.files[0].name;
    var extension = nameFile.match(/.*\.(\w+)/)[1];
    nameFile = JSON.parse(Cookies.get("currentCourse")).courseId + "@" + lectureId + "." + extension.toLowerCase();
    formData.append('file', e.target.files[0], nameFile);
    console.log(nameFile)
    BaseInstance.post("uploadAttendacesPicture", formData)
      .then(res => {
        console.log("upload attendances")
        console.log(res)
        setAttendingStudents(res.data)
        setUploading(false)
      })
  };

  const handleChange = panel => (event, isExpanded) => {
    if(Cookies.get("lecturerId") == Cookies.get("email"))
      setExpanded(isExpanded ? panel : false);
  };

  const deleteLecture = (lectureId) => {
    console.log("deleting " + lectureId)
    BaseInstance.post("deleteLecture", { lectureId: lectureId }).then(res => {
      var currLectures = []
      console.log(res.data)
      for (let i = 0; i < lectures.length; i++) {
        if (lectures[i].lectureId !== lectureId) {
          currLectures.push(lectures[i])
        }
      }
     
      Cookies.set("currentLectures", currLectures)
      setLectures(currLectures)
    })
  }

  const deleteAttendance = (lectureId, email) => {
    BaseInstance.post("deleteAttendance", { lectureId: lectureId, email: email }).then(res => {
      var currStudents = []
      console.log(res.data)
      for (let i = 0; i < attendingStudents.length; i++) {
        if (attendingStudents[i].email !== email) {
          currStudents.push(attendingStudents[i])
        }
      }
      setAttendingStudents(currStudents)
    })
  }

  const getAttendances = (lectureId) => {
    if(Cookies.get("lecturerId") == Cookies.get("email")) {
      BaseInstance.get("getLectureAttendances", { params: { "lectureId": lectureId } }).then(res => {
        setAttendingStudents(res.data)
      })
    }
  }

  return (

    <div className={classes.root}>
      {
        lectures.map(lecture => (
          <ExpansionPanel expanded={expanded === `panel${lecture.lectureId}`} onClick={() => getAttendances(lecture.lectureId)} onChange={handleChange(`panel${lecture.lectureId}`)}>
            <ExpansionPanelSummary
              expandIcon = { Cookies.get("lecturerId") == Cookies.get("email") ? <ExpandMoreIcon /> : null }
              aria-controls={`panel${lecture.lectureId}bh-content`}
              id={`panel${lecture.lectureId}bh-header`}>
              <Typography className={classes.heading}>{lecture.date} - {lecture.description}</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails style={{ paddingBottom: "0px" }} >
              {
                uploading ? (
                  <div className={classes.loading}><CircularProgress color="secondary"/></div>
                ) : (
                    <List style={{ "width": "100%" }}>
                      {
                        attendingStudents.map(student => (
                          <ListItem style={{ paddingTop: "0px", paddingBottom: "0px" }}>
                            <ListItemText>{student.firstName} {student.lastName}</ListItemText>
                            <IconButton edge="end" aria-label="delete" onClick={() => deleteAttendance(lecture.lectureId, student.email)} ><PersonAddDisabledIcon /> </IconButton>
                          </ListItem>
                        ))
                      }
                    </List>
                  )
              }


            </ExpansionPanelDetails>
            <ExpansionPanelActions>
              <IconButton component="label"><AddAPhotoIcon /><input type="file" style={{ display: "none" }} onChange={(e) => onFileChangeHandler(e, lecture.lectureId)} accept=".jpg,.jpeg,.png,.bmp" /></IconButton>
              <ManualAddition lectureId={lecture.lectureId} subscribedStudents={props.subscribedStudents} setSubscribedStudents={props.setSubscribedStudents} attendingStudents={attendingStudents} setAttendingStudents={setAttendingStudents} />
              <IconButton onClick={() => deleteLecture(lecture.lectureId)}><DeleteIcon style={{ color: "#C10000" }} /></IconButton>
            </ExpansionPanelActions>
          </ExpansionPanel>
        ))
      }
      {/*Cookies.get("lecturerId") == Cookies.get("email") ? <LectureCreation setLectures={setLectures} /> : null*/}
      {Cookies.get("lecturerId") == Cookies.get("email") ? <FabGroup material={props.material} setMaterial={props.setMaterial} setNotices={props.setNotices} setLectures={setLectures} /> : null}
    </div>
  );
}