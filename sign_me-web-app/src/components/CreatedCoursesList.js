import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import { useStyles } from './HomeStyle.js';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import ClearOutlinedIcon from '@material-ui/icons/ClearOutlined';
import DeleteIcon from '@material-ui/icons/Delete';
import BaseInstance from '../http-client/BaseInstance.js';
import { IconButton } from '@material-ui/core';
import Cookies from 'js-cookie'
import { createBrowserHistory } from 'history';
import { Link } from 'react-router-dom'

export default function CoursesList(props) {
    const classes = useStyles();

    var courses = props.courses

    const history = createBrowserHistory();
    Cookies.set("currentLectures", [])
    Cookies.set("currentStudents", [])

    const goToCourse = course => {
        Cookies.set("currentCourse", course)

        var courseId = course.courseId
        BaseInstance.get("getCourseInfo", {params: {courseId: courseId}}).then(res => {
            var lectures = res.data.lectures
            var lecturerId = res.data.lecturer
            var students = res.data.users
            var notices = res.data.notices
            var material = res.data.material

            lectures.sort((a, b) => (a.date > b.date) ? 1 : -1)
            students.sort((a,b) => (a.lastName > b.lastName)? 1 : -1)
            notices.sort((a,b) => (a.noticeId > b.noticeId)? 1 : -1)
            material.sort((a,b) => (a.materialId > b.materialId)? 1 : -1)


            Cookies.set("currentLectures", lectures)
            Cookies.set("lecturerId", lecturerId)
            Cookies.set("currentStudents", students)
            Cookies.set("courseNotices", notices)
            Cookies.set("material", material)
                
            history.push("/course");
            window.location.reload()
        })


    }

    const deleteCourse = (id) => {
        BaseInstance.post("deleteCourse", {
            courseId: id
        }).then((res) => {
            var courses = []
            var cookieCourses = JSON.parse(Cookies.get("createdCourses"))
            for(let i=0; i<JSON.parse(Cookies.get("createdCourses")).length; i++){
                if(cookieCourses[i].courseId !== id){
                    courses.push(cookieCourses[i])
                }
            }
            Cookies.set("createdCourses", courses)
            props.setCourses(courses)
        })
    }
return (
  <List component="nav">
            <ListItem className={classes.courseItem} item xs={12}>
                <Typography className={classes.createdCourseNameHeader}>Courses</Typography>
            </ListItem>
            <Divider></Divider>
            <div className={classes.coursesContainer}>
                {courses.map((course) => (
                    <div key={course.courseId}>
                        <ListItem className={classes.courseItem} item xs={12}>
                            <ListItem button>
                                <ListItemText onClick={() => { goToCourse(course) }}>{course.subject}</ListItemText>
                            </ListItem>
                            <IconButton onClick={() => { deleteCourse(course.courseId) }}> <DeleteIcon /> </IconButton>
                        </ListItem>
                        <Divider></Divider>
                    </div>
                ))}
            </div>
        </List>
    )
};