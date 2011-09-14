package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Pastebin extends Model {
	
	public String name;
	
	public String description;
	
	@OneToMany(mappedBy="pastebin", cascade=CascadeType.ALL)
	public List<CodeSnippet> codeSnippets;
	
	public boolean restricted;
	
	public Pastebin(String name) {
		this.name = name;
		this.codeSnippets = new ArrayList<CodeSnippet>();
	}
	
	public static Pastebin findByName(String name) {
		String query = "select pb from Pastebin pb where pb.name = ?";
		Pastebin pastebin = Pastebin.find(query, name).first();
		return pastebin;
	}
	
	public List<CodeSnippet> findSnippetsByUser(long userId) {
		String query = "select cs from Pastebin p join p.codeSnippets cs where p.id = ? and cs.user.id = ? order by cs.timestamp";
		List<CodeSnippet> codeSnippets = Pastebin.find(query, this.id, userId).fetch();
		return codeSnippets;
	}
}
