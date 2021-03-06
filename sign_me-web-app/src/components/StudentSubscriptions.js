import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import WorkIcon from '@material-ui/icons/Work';
import CardContent from '@material-ui/core/CardContent';
import Card from '@material-ui/core/Card';
import BaseInstance from '../http-client/BaseInstance';
import CheckIcon from '@material-ui/icons/Check';
import ClearOutlinedIcon from '@material-ui/icons/ClearOutlined';
import Cookies from 'js-cookie'
import { IconButton } from '@material-ui/core';

export default class StudentSubscriptions extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            subscriptions: [],
            loaded: false
        }
        this.getSubscriptions();
    }

    confirmSubscription = (student) => {
        BaseInstance.post("confirmSubscription", {
            email: student.email,
            course: JSON.parse(Cookies.get("currentCourse")).courseId
        }).then((res) => {
            var students = this.props.subscribedStudents
            students.push(student)
            this.props.setSubscribedStudents(students)
            this.getSubscriptions()
        })
    }

    deleteSubscription = (studentId) => {
        BaseInstance.post("deleteSubscription", {
            email: studentId,
            course: JSON.parse(Cookies.get("currentCourse")).courseId
        }).then((res) => {
            this.getSubscriptions()
        })
    }

    getSubscriptions = () => {
        BaseInstance.post("getSubscriptionRequests", {
            courseId: JSON.parse(Cookies.get("currentCourse")).courseId}).then((res) => {
            var students = [];
            for (var i = 0; i < res.data.length; i++) {
                var student = {
                    "firstName": res.data[i].firstName,
                    "lastName": res.data[i].lastName,
                    "email": res.data[i].email
                };
                students.push(student);
            }
            this.setState({
                subscriptions: students,
                loaded: true
            })
        })
    }

    render() {
        if (this.state.loaded && this.state.subscriptions.length != 0) {
            return (
                <div style={{ padding: "10px" }}>
                    <Card style={{ maxHeight: 500, overflow: 'auto', width: '90%' }}>
                        <CardContent>
                            <List>
                                {
                                    this.state.subscriptions.map(item => (
                                        <ListItem>
                                            <ListItemText style={{ width: 50 }} primary={`${item.firstName} ${item.lastName}`} />
                                            <IconButton onClick={() => { this.confirmSubscription(item) }}> <CheckIcon /> </IconButton>
                                            <IconButton onClick={() => { this.deleteSubscription(item.email) }}> <ClearOutlinedIcon /> </IconButton>
                                        </ListItem>
                                    ))
                                }
                            </List>
                        </CardContent>
                    </Card>
                </div>
            );
        } else {
            return (
                <div style={{ padding: "10px" }}>
                    <Card style={{ maxHeight: 250, overflow: 'auto', width: '90%' }}>
                        <CardContent>
                            <ListItem>
                                <ListItemText>No Subscription Requests</ListItemText>
                            </ListItem>
                        </CardContent>
                    </Card>
                </div>
            );
        }
    }
}