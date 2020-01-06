import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import { useStyles } from './HomeStyle.js';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
//import {BrowserHistory} from 'react-router-dom';

export default function CoursesList(props) {
    const classes = useStyles();
    var courses = props.getSubscribedCourses();
    return (
        <List component="nav">
            <ListItem className={classes.courseItem} item xs={12}>
                <Typography className={classes.courseNameHeader}>Courses</Typography>
                <Typography className={classes.courseAttendaceHeader}>Attendences</Typography>
            </ListItem>
            <Divider></Divider>
            <div className={classes.coursesContainer}>
                {courses.map((course) => (
                    <div key={course.courseId}>
                        <ListItem className={classes.courseItem} xs={12} >
                            <ListItem button onClick={() => { props.goToCoursePage(course) }}>
                                <ListItemText className={classes.courseName} >{course.subject}</ListItemText>
                            </ListItem>
                            <Typography className={classes.courseAttendace}>{course.lectures.length}</Typography>
                        </ListItem>
                        <Divider></Divider>
                    </div>
                ))}
            </div>
        </List>
    )
};