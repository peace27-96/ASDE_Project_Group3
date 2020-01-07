import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import { useStyles } from './HomeStyle.js';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import ClearOutlinedIcon from '@material-ui/icons/ClearOutlined';
import BaseInstance from '../http-client/BaseInstance.js';
import { IconButton } from '@material-ui/core';
import Cookies from 'js-cookie'
import { createBrowserHistory } from 'history';
import { Link } from 'react-router-dom'

export default function CoursesList(props) {
    const classes = useStyles();

    var courses = JSON.parse(Cookies.get("createdCourses"));

    const history = createBrowserHistory();
    Cookies.set("currentLectures", [])
    Cookies.set("currentStudents", [])


    const goToCourse = course => {
        Cookies.set("currentCourse", course)

        var courseId = course.courseId
        BaseInstance.get("getCourseLectures", {params: {courseId: courseId}}).then(res => {
            var lectures = res.data; 
            BaseInstance.get("getCourseStudents", {params: {courseId: courseId}}).then(res => {
                Cookies.set("currentLectures", lectures)
                var students = res.data;
                Cookies.set("currentStudents", students)
            })
        })

    }

    const deleteCourse = (id) => {
        BaseInstance.post("deleteCourse", {
            courseId: id
        }).then((res) => {
            console.log("delete course ")
            console.log(res)
        })
    }
    // <ListItem button style={{ width: 25 }} button onClick={ () => {deleteCourse(course.courseId)}}> <ClearOutlinedIcon/> </ListItem>
    return (
        <List component="nav">
            <ListItem className={classes.courseItem} item xs={12}>
                <Typography className={classes.courseNameHeader}>Courses</Typography>
            </ListItem>
            <Divider></Divider>
            <div className={classes.coursesContainer}>
                {courses.map((course) => (
                    <div key={course.courseId}>
                        <ListItem className={classes.courseItem} item xs={12}>
                            <ListItem button>
                                <Link className={classes.courseName} to="/course" onClick={() => { goToCourse(course) }} style={{ color: "#000000", "text-decoration": "none" }}>{course.subject}</Link>
                            </ListItem>
                            <IconButton onClick={() => { deleteCourse(course.courseId) }}> <ClearOutlinedIcon /> </IconButton>
                        </ListItem>
                        <Divider></Divider>
                    </div>
                ))}
            </div>
        </List>
    )
};