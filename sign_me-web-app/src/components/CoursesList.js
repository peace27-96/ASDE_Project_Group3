import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import {useStyles} from './HomeStyle.js';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import Divider from '@material-ui/core/Divider';
//import {BrowserHistory} from 'react-router-dom';

export default function CoursesList(props) {
    const classes = useStyles();
    var courses = props.getCourses();
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
                        <ListItem className={classes.courseItem} item xs={12}>
                            <Button className={classes.courseName} onClick={ () => { props.goToCoursePage(course) } }>{course.subject}</Button>
                            <Typography className={classes.courseAttendace}>9</Typography>  
                        </ListItem>
                        <Divider></Divider>
                    </div>
                ))}
            </div>
        </List>
    )
};