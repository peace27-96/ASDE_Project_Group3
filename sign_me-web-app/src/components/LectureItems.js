import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { Button, ExpansionPanelActions, Grid } from '@material-ui/core';
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
}));


const onFileChangeHandler = (e, lectureId) => {
  e.preventDefault();
  console.log("upload")
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
      // for current student  
    })
};


export default function ControlledExpansionPanels() {
  const classes = useStyles();
  const [expanded, setExpanded] = React.useState(false)

  const [lectures, setLectures] = React.useState(JSON.parse(Cookies.get("currentLectures")))
  const [students, setStudents] = React.useState([])

  const handleChange = panel => (event, isExpanded) => {
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
      currLectures.sort((a, b) => (a.date > b.date) ? 1 : -1)
      Cookies.set("currentLectures", currLectures)
      setLectures(currLectures)
    })
  }

  const deleteAttendance = (lectureId, email) => {
    BaseInstance.post("deleteAttendance", { lectureId: lectureId, email: email }).then(res => {
      var currStudents = []
      console.log(res.data)
      for (let i = 0; i < students.length; i++) {
        if (students[i].email !== email) {
          currStudents.push(students[i])
        }
      }
      setStudents(currStudents)
    })
  }

  const getAttendaces = (lectureId) => {
    BaseInstance.get("getLectureAttendances", { params: { "lectureId": lectureId } }).then(res => {
      setStudents([])
      console.log(res.data)
      setStudents(res.data)
      console.log(students)
    })
  }

  return (

    <div className={classes.root}>
      {
        lectures.map(lecture => (
          <ExpansionPanel expanded={expanded === `panel${lecture.lectureId}`} onClick={() => getAttendaces(lecture.lectureId)} onChange={handleChange(`panel${lecture.lectureId}`)}>
            <ExpansionPanelSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls={`panel${lecture.lectureId}bh-content`}
              id={`panel${lecture.lectureId}bh-header`}>
              <Typography className={classes.heading}>{lecture.date} - {lecture.description}</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails>
              <List>
                {
                  students.map(student => (
                    <ListItem>
                      <ListItemText style={{ width: 80 }}>{student.firstName} {student.lastName}</ListItemText>
                      <IconButton edge="end" aria-label="delete" onClick={() => deleteAttendance(lecture.lectureId, student.email)} ><PersonAddDisabledIcon /> </IconButton>
                    </ListItem>

                  ))
                }
              </List>
            </ExpansionPanelDetails>
            <ExpansionPanelActions>
              <IconButton component="label"><AddAPhotoIcon /><input type="file" style={{ display: "none" }} onChange={(e) => onFileChangeHandler(e, lecture.lectureId)} accept=".jpg,.jpeg,.png,.bmp" /></IconButton>
              <ManualAddition lectureId={lecture.lectureId} students={students} />
              <IconButton onClick={() => deleteLecture(lecture.lectureId)}><DeleteIcon style={{ color: "#C10000" }} /></IconButton>
            </ExpansionPanelActions>
          </ExpansionPanel>
        ))
      }
      <LectureCreation setLectures={setLectures} />
    </div>
  );
}