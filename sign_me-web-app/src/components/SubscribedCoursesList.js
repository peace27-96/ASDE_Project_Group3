import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import { useStyles } from './HomeStyle.js';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import { createBrowserHistory } from 'history';
import Cookies from 'js-cookie'
import {Link} from 'react-router-dom'
import BaseInstance from '../http-client/BaseInstance';

export default function CoursesList() {
    const classes = useStyles();
    var courses = JSON.parse(Cookies.get("subscribedCourses"));
    const [count, setCount] = React.useState(0)
    const history = createBrowserHistory()
    const goToCourse = course => {
        Cookies.set("currentCourse", course)
        //history.push("/course")
    }


    const getAttendancesNumber = (courseId) => {
        BaseInstance.get("getAttendancesNumber", {params:{email:Cookies.get("email"), courseId:courseId}}).then ( res => {
            console.log(res.data)
            setCount(res.data)
        }) 
        return count
    }

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
                            <ListItem button >
                               <Link className={classes.courseName} to="/course" onClick={() => { goToCourse(course) }} style={{color:"#000000","text-decoration": "none"}}>{course.subject}</Link>
                            </ListItem>
                            <Typography className={classes.courseAttendace}>{getAttendancesNumber(course.courseId)}</Typography>
                        </ListItem>
                        <Divider></Divider>
                    </div>
                ))}
            </div>
        </List>
    )
};