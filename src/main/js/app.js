'use strict';

// tag::vars[]
const React = require('react'); // <1>
const ReactDOM = require('react-dom'); // <2>
const client = require('./client'); // <3>
// end::vars[]

// tag::app[]
class App extends React.Component { // <1>

    constructor(props) {
        super(props);
        this.state = {users: []};
    }

    componentDidMount() { // <2>
        client({method: 'GET', path: '/api/admin/users'}).done(response => {
            this.setState({users: response.entity._embedded.users});
        });
    }

    render() { // <3>
        return (
            <UserList users={this.state.users}/>
    )
    }
}
// end::app[]

// tag::employee-list[]
class UserList extends React.Component{
    render() {
        const users = this.props.users.map(users =>
            <User key={user._links.self.href} user={user}/>
    );
        return (
            <table>
            <tbody>
            <tr>
            <th>Email</th>
        <th>Username</th>
        <th>Blocked</th>
        </tr>
        {users}
    </tbody>
        </table>
    )
    }
}
// end::employee-list[]

// tag::employee[]
class User extends React.Component{
    render() {
        return (
            <tr>
            <td>{this.props.user.email}</td>
            <td>{this.props.user.username}</td>
            <td>{this.props.user.blocked}</td>
            </tr>
    )
    }
}
// end::employee[]

// tag::render[]
ReactDOM.render(
<App />,
    document.getElementById('react')
)
// end::render[]